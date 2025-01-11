package com.glassthought.sandbox

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

// Base class
@Serializable
@Polymorphic
sealed class Event(
  val eventType: String,
  val creationId: Long = System.currentTimeMillis()
)

// Subclass with extra fields
@Serializable
data class FileChangeEvent(
  val filePath: String,
  val updateType: String,
) : Event("eventTypeVal")

// Utility for JSON serialization
object JsonUtil {
  private val module = SerializersModule {
    polymorphic(Event::class) {
      subclass(FileChangeEvent::class)
    }
  }

  val json = Json {
    serializersModule = module
    encodeDefaults = true
    classDiscriminator = "type" // Field to distinguish subclasses
  }

  fun toJson(event: Event): String {
    return json.encodeToString(Event.serializer(), event)
  }

  inline fun <reified T : Event> fromJson(jsonString: String): T {
    return json.decodeFromString(Event.serializer(), jsonString) as T
  }
}

// Main function to test serialization and deserialization
fun main() {
  val fileChangeEvent = FileChangeEvent(
    filePath = "/tmp/test.txt",
    updateType = "Created"
  )

  // Serialize the subclass
  val json = JsonUtil.toJson(fileChangeEvent)
  println("Serialized JSON:")
  println(json)

  // Deserialize the JSON back to the appropriate subclass
  val deserializedEvent = JsonUtil.fromJson<FileChangeEvent>(json)
  println("Deserialized Object:")
  println(deserializedEvent)
}
