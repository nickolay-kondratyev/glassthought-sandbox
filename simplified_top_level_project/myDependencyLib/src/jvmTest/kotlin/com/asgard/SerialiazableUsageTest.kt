package com.asgard

import io.kotest.core.spec.style.DescribeSpec
/*import kotlinx.serialization.Serializable

@Serializable
data class TestClass(val name: String, val value: Int)*/

class SerialiazableUsageTest: DescribeSpec(
    {
        describe("a test") {
            it("should pass") {
                println("Hello, world!")
            }
        }
    }
)
