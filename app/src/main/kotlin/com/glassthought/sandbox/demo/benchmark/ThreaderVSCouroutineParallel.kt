package com.glassthought.sandbox.demo.benchmark

import com.asgard.core.testTools.tmpTestDir
import com.asgard.core.threading.runInParallel
import com.asgard.core.threading.threader
import com.asgard.testTools.benchmarker.BenchMarkerJVM
import com.glassthought.sandbox.FlexmarkUtils
import com.thorg.test.integsetup.TestNoteProvider
import kotlin.time.Duration.Companion.seconds

class ParsingBenchmarker(
  private val numberOfTimesToBenchmark: Int = 1000,
  private val howManyNotes: Int = 2000,
  private val warmupIterations: Int = 5
) {
  private val parser = FlexmarkUtils.createStandardParser()

  // Let's not be concerned about file markdown loading, as notes should
  // be in memory by the time we parse markdown.
  private val contentList = getContentForXNotes(howManyNotes)

  suspend fun go() {


    benchMark({
      // Important to collect parallel streams otherwise it creates a stream that doesn't
      // yet execute (so it looks unrealistically fast)
      contentList.parallelStream().map { content ->
        parser.parse(content)
      }.toList()
    }, "Parsing directly with java parallel stream [parallelStream().map{parse()}]")

    benchMark({
      threader().runInParallel(
        contentList.map { content ->
          {
            parser.parse(content)
          }
        }.toList()
      )
    }, "Parsing with our java threader()")


    benchMark({
      contentList.map { content ->
        suspend {
          parser.parse(content)
        }
      }.runInParallel(timeout = 5.seconds)
    }, "Parsing suspend/runInParallel")

    benchMark({
      contentList.map { content ->
        parser.parse(content)
      }
    }, "Parsing serially [.map{parse()}]")


  }

  private suspend fun benchMark(runnable: suspend () -> Unit, descriptionPrefix: String) {
    val result = BenchMarkerJVM.benchMark(
      runnable,
      numberOfTimes = numberOfTimesToBenchmark,
      warmupIterations = warmupIterations,
      description = "$descriptionPrefix howManyNotes=[$howManyNotes], warmupIterations=[$warmupIterations]"
    )

    result.printFormattedTable()
  }
}

suspend fun main(args: Array<String>) {
  ParsingBenchmarker().go()
}

private fun getContentForXNotes(howManyNotes: Int): List<String> {
  val copied = TestNoteProvider.instance()
    .preCopyToStaging(howManyNotes, tmpTestDir())
    .stagedAsIs.copyToDestinationDirectory()

  val markdownContent = copied.testNotes
    .map { it.file }
    .map { it.readText() }

  return markdownContent
}
