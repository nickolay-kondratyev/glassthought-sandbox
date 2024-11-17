package com.glassthought.sandbox

import gt.sandbox.util.output.print
import gt.sandbox.util.output.printGreen
import gt.sandbox.util.output.println
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal


// Declare a sequence of Fibonacci numbers, using BigDecimal
// to handle large values.
val fibonacci: Sequence<BigDecimal> = sequence {
  // Initialize the first two Fibonacci numbers.
  var current = 1.toBigDecimal()  // Current number in the sequence
  var prev = 1.toBigDecimal()     // Previous number in the sequence

  // Emit the first Fibonacci number (1).
  yield(prev)

  // Use an infinite loop to generate subsequent numbers.
  while (true) {
    // Emit the current Fibonacci number.
    yield(current)

    // Compute the next Fibonacci number by adding the previous two numbers.
    val temp = prev        // Temporarily store the previous number
    prev = current         // Update the previous number to the current one
    current += temp        // Update the current number to the sum of current and previous
  }
}

fun main() = runBlocking {
  fibonacci.take(10).toList().println()

  System.out.flush()
}
