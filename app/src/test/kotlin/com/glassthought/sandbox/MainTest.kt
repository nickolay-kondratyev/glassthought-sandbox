package com.glassthought.sandbox

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


var afterInnerSpecCounter = 0

class ExampleDescribeSpecTest : DescribeSpec({
  describe("Mathematical operations") {
    describe("inner describe block") {
      afterContainer {
        println("Cleaning up after the inner describe block: called ${++afterInnerSpecCounter} times")
      }

      it("test1") {
        println("test1")
      }

      it("test2") {
        println("test2")

      }
    }


    it("should subtract two numbers") {
      println("hi")
    }
  }
})
