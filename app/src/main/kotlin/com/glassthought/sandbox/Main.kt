package com.glassthought.sandbox

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

// Base class
@Serializable
open class Event(
  val eventType: String,
  val creationId: Long = System.currentTimeMillis()
) {
  fun toJson(): String {
    // Incorrect serialization: Only serializes fields of `Event`
    return JsonUtil.toJson(serializer(), this)
  }
}

// Subclass
@Serializable
class FileChangeEvent(
  val filePath: String,
  val updateType: String
) : Event("FileChange")

// Utility object with custom Json configuration
object JsonUtil {
  private val module = SerializersModule {
    polymorphic(Event::class) {
      subclass(FileChangeEvent::class)
    }
  }

  val json = Json {
    serializersModule = module
    encodeDefaults = true
  }

  fun <T> toJson(serializer: SerializationStrategy<T>, value: T): String {
    return json.encodeToString(serializer, value)
  }
}

// Main function to emulate the problem
fun main() {
  val fileChangeEvent = FileChangeEvent(
    filePath = "/tmp/test.txt",
    updateType = "Created"
  )

  // Incorrect serialization: Only base fields serialized
  println("Incorrect serialization:")
  println(fileChangeEvent.toJson()) // Outputs: {"eventType":"FileChange","creationId":...}
}
