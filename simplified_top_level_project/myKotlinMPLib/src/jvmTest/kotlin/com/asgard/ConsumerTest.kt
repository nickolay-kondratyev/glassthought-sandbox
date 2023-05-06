package com.asgard

import asgard.ConsumerImplementationDependency
import io.kotest.core.spec.style.DescribeSpec

class ConsumerTest : DescribeSpec({
    describe("GIVEN starter describe block") {
        describe("WHEN running tests") {
            it("THEN 1+1 equals 2") {
                assert(1 + 1 == 2)
            }
        }

        it("WHEN calling start implementation THEN should respond with expected number") {
            val starter = ConsumerImplementationDependency()

            assert(starter.getInteger() == 42000)
        }

    }
})
