package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val out = Out.standard()

/**
 * SyncFlags is a synchronization utility for tracking multiple named flags.
 * Flags represent states or conditions that must be completed before proceeding.
 */
class SyncFlags {
  private val flags = mutableMapOf<String, CompletableDeferred<Unit>>()
  private val mutex = Mutex() // Ensure thread-safe access to flags

  /**
   * Registers a new flag with the given name.
   * If the flag already exists, this will have no effect.
   */
  suspend fun registerFlag(name: String) {
    mutex.withLock {
      if (!flags.containsKey(name)) {
        flags[name] = CompletableDeferred()
      } else {
        throw IllegalStateException("Flag '$name' is already registered.")
      }
    }
  }

  /**
   * Marks the flag with the given name as completed.
   * Throws an exception if the flag does not exist.
   */
  suspend fun completeFlag(name: String) {
    out.info("Completing flag: $name")

    mutex.withLock {
      flags[name]?.complete(Unit)
        ?: throw IllegalStateException("Flag '$name' is not registered.")
    }
  }

  /**
   * Waits for the specified flags to complete.
   * Throws an exception if any flag is not registered.
   */
  suspend fun waitForFlags(flagNames: List<String>) {
    flagNames.forEach { name ->
      // Very important not to hold the mutex while awaiting the flag
      // as that is a sure way to deadlock and not allow some other thread to complete the flag.
      mutex.withLock {
        flags[name] ?: throw IllegalStateException("Flag '$name' is not registered.")
      }

      flags[name]?.await()
    }
  }

  /**
   * Checks if a flag has been completed.
   */
  suspend fun isFlagComplete(name: String): Boolean {
    return mutex.withLock {
      flags[name]?.isCompleted ?: false
    }
  }
}

suspend fun main(args: Array<String>) {
  out.info("Starting the application")

  val syncFlags = SyncFlags()

  syncFlags.registerFlag("Flag1")
  syncFlags.registerFlag("Flag2")

  val scope = CoroutineScope(Dispatchers.Default)

  val job1 = scope.launch(CoroutineName("UpdateNews")) {

    out.info("Sleeping")
    Thread.sleep(1000)

    syncFlags.completeFlag("Flag1")
    Thread.sleep(1000)
    syncFlags.completeFlag("Flag2")

  }

  val job2 = scope.launch(CoroutineName("WaitForAllStates")) {
    out.info("Starting to wait for all flags.")

    syncFlags.waitForFlags(
      listOf("Flag1", "Flag2")
    )

    out.info("Done waiting.")
  }

  job1.join()
  job2.join()
}

