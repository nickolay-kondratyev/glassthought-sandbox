package com.example.demo

import kotlin.js.JsExport

@JsExport
class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}! [This is written in Kotlin]"
    }
}
