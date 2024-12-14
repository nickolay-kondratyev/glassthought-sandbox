package com.glassthought.sandbox

import com.glassthought.sandbox.sandbox.util.out.impl.OutSettings
import gt.sandbox.util.output.Out
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

val out = Out.standard(OutSettings(printCoroutineName = true))

class MyTestExtension : TestCaseExtension {
  override suspend fun intercept(
    testCase: TestCase,
    execute: suspend (TestCase) -> TestResult
  ): TestResult {
    out.printlnBlue("Before executing test: ${testCase.name.testName}")

    val result = execute(testCase)
    out.printlnGreen("After executing test: ${testCase.name.testName}")
    return result
  }
}

class MyDescribeSpec : DescribeSpec({
  extension(MyTestExtension())

  describe("outer-describe") {
    describe("nested-describe-1"){
      it("it-in-nested-describe") {
        out.println("Running test inside inner describe")
      }
    }
    it("test-1 in outer describe") {
      out.println("running test-1 in outer describe")
    }

    it("test-2 in outer describe") {
      out.println("Running test-2 in outer describe")
    }
  }
})
