package com.glassthought.sandbox

import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult


class MyTestExtension : TestCaseExtension {
  override suspend fun intercept(
    testCase: TestCase,
    execute: suspend (TestCase) -> TestResult
  ): TestResult {
    println()
    println("Before executing test: ${testCase.name}")
    val result = execute(testCase)
    println("After executing test: ${testCase.name}, Result: $result")
    return result
  }
}

class MyDescribeSpec : DescribeSpec({
  extension(MyTestExtension())

  describe("A group of tests") {
    it("should pass test 1") {
      println("Running test 1")
    }

    it("should pass test 2") {
      println("Running test 2")
    }
  }
})
