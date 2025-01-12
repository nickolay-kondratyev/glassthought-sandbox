package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

object SealedClassAsserter {
  fun assertAllChildClassesAreSerializable(sealedClass: KClass<*>) {
    require(sealedClass.isSealed) {
      "Class ${sealedClass.simpleName} is not a sealed class"
    }

    val childClasses = sealedClass.sealedSubclasses

    val nonSerializableClasses = childClasses.filterNot { it.isSerializable() }

    if (nonSerializableClasses.isNotEmpty()) {
      val nonSerializableNames = nonSerializableClasses.joinToString(", ") { it.simpleName ?: "Unnamed class" }
      throw VerificationError("The following subclasses of ${sealedClass.simpleName} are not @Serializable: $nonSerializableNames")
    }
  }

  private fun KClass<*>.isSerializable(): Boolean {
    // Check if the class is annotated with @Serializable
    return this.annotations.any { it.annotationClass == Serializable::class }
  }

  class VerificationError(message: String) : RuntimeException(message)
}

val out = Out.standard()

// Base class
@Serializable
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
}

// Main function to test serialization and deserialization
fun main() {
  SealedClassAsserter.assertAllChildClassesAreSerializable(
    Event::class
  )

  val filePath = "/tmp/test.txt"
  val fileChangeEvent = FileChangeEvent(
    filePath = filePath,
    updateType = "Created"
  )

  // Serialize the subclass
  val json = JsonUtil.toJson(fileChangeEvent)
  println("Serialized JSON:")
  println(json)

  if (!json.contains("eventTypeVal")) {
    out.printlnRed("Does not contain parent value")
  } else {
    out.printlnGreen("Contains parent value")
  }
  if (!json.contains("creationId")) {
    out.printRed("Does not contain parent value")
  } else {
    out.printlnGreen("Contains parent value")
  }
  if (!json.contains(filePath)) {
    out.printRed("Does not contain filePath")
  } else {
    out.printlnGreen("Contains filePath")
  }
}
