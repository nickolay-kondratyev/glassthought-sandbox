package com.asgard

import asgard.ConsumerImplementationDependency
import io.kotest.core.spec.style.DescribeSpec

class ConsumerTest : DescribeSpec({
    describe("GIVEN starter describe block") {


        it("IMPORTANT WHEN calling consumer get number from dependency.") {
            val starter = ConsumerImplementationDependency()

            assert(starter.getInteger() == 42000)
        }

    }
})
