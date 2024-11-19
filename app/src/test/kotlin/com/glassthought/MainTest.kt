package com.glassthought

import io.kotest.core.spec.style.DescribeSpec

class DescribeSpecExample : DescribeSpec({
  var counter = 0

  describe("Counter example for DescribeSpec") {
    counter++
    println("Counter in describe: $counter")

    it("First IT block") {
      println("Counter in First IT: $counter")
    }

    it("Second IT block") {
      println("Counter in Second IT: $counter")
    }

    describe("Nested Describe") {
      it("Nested IT block") {
        println("Counter in Nested IT: $counter")
      }
    }
  }
})
