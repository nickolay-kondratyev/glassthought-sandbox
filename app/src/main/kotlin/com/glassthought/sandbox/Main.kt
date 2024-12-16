package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.time.measureTime

val out = Out.standard()

/**
 * Provides functionality to run multiple suspend tasks in parallel
 * and gather their results.
 *
 * WHY:
 * Hide coroutine complexity and allow users to easily parallelize long-running tasks
 * (e.g., generating images) in a Kotlin Multiplatform-friendly way.
 */
interface ParallelRunner {
  /**
   * Run given tasks in parallel and return their results.
   *
   * @param tasks The list of suspend functions representing the work.
   * @return The list of results in the same order as tasks.
   */
  suspend fun <T> runInParallel(tasks: List<suspend () -> T>): List<T>

  companion object {
    fun standard(): ParallelRunner = DefaultParallelRunner.standard()
  }
}

/**
 * Default implementation of [ParallelRunner] that runs tasks in parallel on the Default dispatcher.
 */
class DefaultParallelRunner : ParallelRunner {
  override suspend fun <T> runInParallel(tasks: List<suspend () -> T>): List<T> = coroutineScope {
    // WHY: Using async for each task to run them concurrently.
    val deferred = tasks.map { task ->
      async(Dispatchers.IO) {
        task()
      }
    }

    // WHY: Using awaitAll to wait for all tasks to complete and return their results.
    deferred.awaitAll()
  }

  companion object {
    private val instance = DefaultParallelRunner()

    fun standard(): DefaultParallelRunner = instance
  }
}

/**
 * Extension function for convenience.
 *
 * Allows calling `listOfTasks.runInParallel()` directly.
 */
suspend fun <T> List<suspend () -> T>.runInParallel(
  runner: ParallelRunner = ParallelRunner.standard()
): List<T> {
  return runner.runInParallel(this)
}

// ------------------------------------------------------------------------------------------
// USAGE EXAMPLE:
suspend fun urlGeneratorFunctionImageineGPTCall(prompt: String): String {
  out.info("Generating image for prompt: $prompt")
  delay(2000)

  return "[Imagine this is URL for prompt: $prompt]"
}

fun main(args: Array<String>) {
  runBlocking {
    out.info("Starting the process on main")

    // Suppose we have multiple prompts we want to generate images for
    val prompts =
      listOf(
        "A cute realistic baby sea otter",
        "A scenic mountain view",
        "A futuristic city skyline"
      )

    val tasks = prompts.map { prompt ->
      // 'suspend' is added here to create a lambda, to be passed to runInParallel later
      // instead of calling the function directly.
      suspend {
        urlGeneratorFunctionImageineGPTCall(prompt)
      }
    }

    val duration = measureTime {
      val results = tasks.runInParallel()

      results.forEach { url ->
        out.info("Image URL: $url")
      }
    }

    out.info("Total time taken: $duration")
  }

}

