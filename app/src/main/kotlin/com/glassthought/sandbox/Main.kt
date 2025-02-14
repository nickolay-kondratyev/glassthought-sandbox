package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import gt.sandbox.util.output.Out

fun main() = runBlocking {
    val channel = Channel<String>()
    val out = Out.standard()

    // Launch a coroutine to send messages
    launch {
        val messages = listOf("Hello", "from", "Kotlin", "Channels")
        for (msg in messages) {
            channel.send(msg)
            delay(500) // Simulate some work
        }
        channel.close() // Close the channel when done
    }

    // Launch a coroutine to receive messages
    launch {
        for (msg in channel) {
            out.println(msg) // Use Out interface for output
        }
    }
}

