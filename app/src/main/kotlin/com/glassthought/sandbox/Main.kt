package com.glassthought.sandbox

// Define a generic interface for handlers
interface Handler {
  fun handleEvent()
}

fun main() {

  // Implement two concrete handlers
  class ConcreteHandlerA : Handler {
    override fun handleEvent() {
      println("ConcreteHandlerA handling event.")
    }

    override fun toString(): String = "ConcreteHandlerA"
  }

  class ConcreteHandlerB : Handler {
    override fun handleEvent() {
      println("ConcreteHandlerB handling event.")
    }

    override fun toString(): String = "ConcreteHandlerB"
  }

  // A list to store unique handlers
  val registeredHandlers = mutableListOf<Handler>()

  // Function to add a handler, ensuring no duplicate references
  fun registerHandler(handler: Handler) {
    if (registeredHandlers.none { it === handler }) { // Check for reference equality
      registeredHandlers.add(handler)
      println("Registered: $handler")
    } else {
      println("Duplicate detected not registered: $handler")
    }
  }

  // Create handler instances
  val handlerA = ConcreteHandlerA()
  val handlerB = ConcreteHandlerB()
  val duplicateHandlerA = handlerA // Same reference as handlerA

  // Register handlers
  registerHandler(handlerA) // Should register
  registerHandler(handlerB) // Should register
  registerHandler(duplicateHandlerA) // Should not register (same reference as handlerA)

  // Print the final list of registered handlers
  println("Registered handlers: $registeredHandlers")
}
