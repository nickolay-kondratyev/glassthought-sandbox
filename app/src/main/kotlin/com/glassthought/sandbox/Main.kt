package com.glassthought.sandbox

import com.sun.org.apache.bcel.internal.util.Args.require

// The following code is a self-contained example demonstrating Kotlin inline/value classes.

// Define an inline class for Email to enforce type safety and encapsulate email-specific logic.
@JvmInline
value class Email(val address: String) {
  // Basic validation in the initializer ensures the email contains an '@' symbol.
  init {
    require(address.contains("@")) { "Invalid email address: $address" }
  }

  /**
   * Obfuscates the email address by hiding part of the local segment.
   *
   * @return A partially masked email address for privacy.
   */
  fun obfuscate(): String {
    // Split the email into local and domain parts.
    val parts = address.split("@")
    // Return a masked version with only the first two characters of the local part visible.
    return "${parts[0].take(2)}***@${parts[1]}"
  }
}

/**
 * Simulates sending a welcome email.
 *
 * @param email A type-safe Email instance.
 */
fun sendWelcomeEmail(email: Email) {
  // The use of Email ensures that the provided address is valid by design.
  println("Sending welcome email to: ${email.address}")
}


/**
 * Main entry point for the application.
 */
fun main() {
  try {
    // Create a valid Email inline class instance.
    val userEmail = Email("alice@example.com")
    sendWelcomeEmail(userEmail)
    println("Obfuscated email: ${userEmail.obfuscate()}")

    // Uncomment the following lines to test error handling with an invalid email:
    // val invalidEmail = Email("invalidEmail")
    // sendWelcomeEmail(invalidEmail)
  } catch (ex: IllegalArgumentException) {
    // Handle invalid email format errors.
    println("Error: ${ex.message}")
  }
}
