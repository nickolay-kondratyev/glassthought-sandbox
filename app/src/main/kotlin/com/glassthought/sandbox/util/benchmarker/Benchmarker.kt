package com.glassthought.sandbox.util.benchmarker

import kotlin.math.ceil

/** GPT o1 created. I have NOT done thorough review of it. */
object Benchmarker {

  /**
   * Benchmark the given [block] for [times] iterations.
   *
   * If the lambda is so fast that `System.nanoTime()` returns 0 for a single call,
   * we switch to a total-timing approach and only compute average, min, max.
   */
  suspend fun benchmark(
    block: suspend () -> Unit,
    name: String? = null,
    times: Int = 1_000_000
  ): BenchmarkResult {
    require(times > 0) { "Number of times to execute must be > 0." }

    // Measure one call first to see if we get a measurable duration:
    val firstStart = System.nanoTime()
    block()
    val firstEnd = System.nanoTime()
    val firstCallNanos = firstEnd - firstStart

    // If first call is too fast (duration == 0), do total timing:
    if (firstCallNanos == 0L) {
      val totalStart = System.nanoTime()
      repeat(times - 1) {
        block()
      }
      val totalEnd = System.nanoTime()
      val totalNanos = totalEnd - totalStart + firstCallNanos // add in first call's measured 0

      val averageNs = totalNanos / times
      // min=average, max=average, all percentiles = null
      return BenchmarkResult(
        name = name,
        timesExecuted = times,
        totalTimeNs = totalNanos,
        averageNs = averageNs,
        minNs = averageNs,
        maxNs = averageNs,
        p50Ns = null,
        p90Ns = null,
        p99Ns = null,
        p999Ns = null,
        p100Ns = null
      )
    }

    // If we can measure single calls, let's gather durations:
    val durations = LongArray(times)
    durations[0] = firstCallNanos
    for (i in 1 until times) {
      val start = System.nanoTime()
      block()
      val end = System.nanoTime()
      durations[i] = end - start
    }

    // Compute total
    val totalNanos = durations.sum()
    // Sort for percentile calculations
    durations.sort()

    val averageNs = totalNanos / times
    val minNs = durations.first()
    val maxNs = durations.last()

    val p50Ns = percentile(durations, 50.0)
    val p90Ns = percentile(durations, 90.0)
    val p99Ns = percentile(durations, 99.0)
    val p999Ns = percentile(durations, 99.9)
    val p100Ns = durations.last() // same as max

    return BenchmarkResult(
      name = name,
      timesExecuted = times,
      totalTimeNs = totalNanos,
      averageNs = averageNs,
      minNs = minNs,
      maxNs = maxNs,
      p50Ns = p50Ns,
      p90Ns = p90Ns,
      p99Ns = p99Ns,
      p999Ns = p999Ns,
      p100Ns = p100Ns
    )
  }

  /**
   * Simple percentile helper. p in [0..100]
   */
  private fun percentile(sortedDurations: LongArray, p: Double): Long {
    if (sortedDurations.isEmpty()) return 0L

    // e.g. for p=50 -> median
    // Implementation detail:
    // index = ceil((p / 100) * N) - 1
    // but ensure it doesn't go out of bounds
    val n = sortedDurations.size
    val rank = (p / 100.0) * n
    val idx = ceil(rank).toInt() - 1
    return sortedDurations[idx.coerceIn(0, n - 1)]
  }
}
