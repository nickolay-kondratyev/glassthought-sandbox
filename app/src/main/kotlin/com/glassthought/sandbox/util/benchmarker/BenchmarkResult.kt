package com.glassthought.sandbox.util.benchmarker

/**
 * Data class holding the benchmark results.
 */
data class BenchmarkResult(
  val timesExecuted: Int,
  val totalTimeNs: Long,

  val averageNs: Long,
  val minNs: Long,
  val maxNs: Long,

  val p50Ns: Long?,
  val p90Ns: Long?,
  val p99Ns: Long?,
  val p999Ns: Long?,
  val p100Ns: Long?
) {
  /**
   * Returns a human-readable string for this result.
   */
  override fun toString(): String {
    val sb = StringBuilder()
    sb.appendLine("=== Benchmark Results ===")
    sb.appendLine("Executed $timesExecuted times")
    sb.appendLine("Total time:  ${formatNanos(totalTimeNs)}")

    sb.appendLine("Average:     ${formatNanos(averageNs)}")
    sb.appendLine("Min:         ${formatNanos(minNs)}")
    sb.appendLine("Max:         ${formatNanos(maxNs)}")

    // For percentile fields, only show them if not null
    if (p50Ns != null) {
      sb.appendLine("P50:         ${formatNanos(p50Ns)}  (${runsPerMsString(p50Ns)})")
    } else {
      sb.appendLine("P50:         null")
    }

    if (p90Ns != null) {
      sb.appendLine("P90:         ${formatNanos(p90Ns)}  (${runsPerMsString(p90Ns)})")
    } else {
      sb.appendLine("P90:         null")
    }

    if (p99Ns != null) {
      sb.appendLine("P99:         ${formatNanos(p99Ns)}  (${runsPerMsString(p99Ns)})")
    } else {
      sb.appendLine("P99:         null")
    }

    if (p999Ns != null) {
      sb.appendLine("P99.9:       ${formatNanos(p999Ns)}  (${runsPerMsString(p999Ns)})")
    } else {
      sb.appendLine("P99.9:       null")
    }

    if (p100Ns != null) {
      sb.appendLine("P100:        ${formatNanos(p100Ns)}  (${runsPerMsString(p100Ns)})")
    } else {
      sb.appendLine("P100:        null")
    }

    return sb.toString()
  }

  /**
   * Helper to produce a human-readable string from nanoseconds,
   *   scaling to ns, µs, ms, s if appropriate.
   */
  private fun formatNanos(ns: Long): String {
    // Edge cases
    if (ns == 0L) return "0 ns"

    var value = ns.toDouble()
    var unit = "ns"

    // scale to microseconds
    if (value >= 1000) {
      value /= 1000
      unit = "microseconds"

      // scale to milliseconds
      if (value >= 1000) {
        value /= 1000
        unit = "milliseconds"

        // scale to seconds
        if (value >= 1000) {
          value /= 1000
          unit = "seconds"
        }
      }
    }

    // For consistent formatting up to 3 decimals if needed
    return String.format("%.3f $unit", value).replace(".000", "")
  }

  /**
   * Returns how many times we can run in one millisecond for
   * a given duration in nanoseconds.
   *
   * 1 ms = 1,000,000 ns
   */
  private fun runsPerMsString(ns: Long): String {
    return if (ns == 0L) {
      "(∞ runs/ms)"
    } else {
      val runsPerMs = 1_000_000.0 / ns
      "(${String.format("%.3f runs/ms", runsPerMs)})"
    }
  }
}
