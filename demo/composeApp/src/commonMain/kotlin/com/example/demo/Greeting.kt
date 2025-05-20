package com.example.demo

import kotlin.js.JsExport // Import JsExport
import kotlin.js.ExperimentalJsExport // Import ExperimentalJsExport

@OptIn(ExperimentalJsExport::class)
@JsExport // Export the Greeting class to JavaScript
class Greeting {
    private val platform = getPlatform()
    
    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}