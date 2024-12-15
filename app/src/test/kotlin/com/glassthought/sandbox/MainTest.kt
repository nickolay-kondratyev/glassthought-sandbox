package com.glassthought.sandbox

import com.glassthought.sandbox.impl.CustomDescribeSpec
import com.glassthought.sandbox.impl.out


class MyCustomDescribeSpec : CustomDescribeSpec({
  describe("outer-describe") {
    describe("nested-describe-1") {
      it("it-in-nested-describe") {
        out.println("Running test inside inner describe")
      }
    }
    it("test-1 in outer describe") {
      out.println("Running test-1 in outer describe")
    }
    it("test-2 in outer describe") {
      out.println("Running test-2 in outer describe")
    }
  }
})
