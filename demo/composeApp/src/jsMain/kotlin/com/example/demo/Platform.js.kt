package com.example.demo

import kotlin.js.JsExport // Import JsExport

// Actual implementation for JavaScript target
class JsPlatform : Platform {
    override val name: String = "JavaScript (Browser)"
}

actual fun getPlatform(): Platform = JsPlatform()

// If you want to directly call getPlatform() from JS, you can export it:
// @JsExport
// fun getPlatformJs(): Platform { // Use a different name or ensure no clashes if commonMain's getPlatform is also exported
//     return getPlatform()
// } 