package gt.sandbox

import gt.sandbox.util.output.Out

val out = Out.standard()

class ComplexCalculation {
    // A lazily initialized property
    val expensiveValue: Int by lazy {
        out.println("Computing the value...")
        performComplexCalculation()
    }

    private fun performComplexCalculation(): Int {
        // Simulate a complex calculation
        Thread.sleep(1000) // simulate a delay
        return 42 // return some computed value
    }
}

fun main() {
    val calculation = ComplexCalculation()

    // The expensiveValue is not computed until it's accessed for the first time
    out.println("Before accessing expensiveValue")
    out.println("The value is: ${calculation.expensiveValue}") // This triggers the calculation
    out.println("After accessing expensiveValue")
    out.println("Access it again does not trigger re-calculation: ${calculation.expensiveValue}") // This does not trigger the calculation
}
