package com.glassthought.sandbox

import io.kotest.core.spec.style.DescribeSpec

var counter = 0

class DescribeSpecExample : DescribeSpec({

  describe("Counter example for DescribeSpec") {
    beforeTest {
      counter++
    }

    println("Counter in outer describe: $counter")

    it("First IT block") {
      println("Counter in First IT: $counter")
    }

    it("Second IT block") {
      println("Counter in Second IT: $counter")
    }

    describe("Nested Describe") {
      println("Counter in nested describe: $counter")

      it("Nested IT block-1") {
        println("Counter in Nested IT-1: $counter")
      }

      it("Nested IT block") {
        println("Counter in Nested IT-2: $counter")
      }
    }
  }
})
