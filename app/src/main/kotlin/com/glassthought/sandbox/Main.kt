package com.glassthought.sandbox

import gt.sandbox.util.output.out
import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread


class DirectoryWatchingUtility(private val directoryToWatch: Path) {
  private val watcher: DirectoryWatcher = DirectoryWatcher.builder()
    .path(directoryToWatch) // or use paths(directoriesToWatch)
    .listener { event: DirectoryChangeEvent ->
      when (event.eventType()) {
        DirectoryChangeEvent.EventType.CREATE -> out.info("Event caught-File created: " + event.path())
        DirectoryChangeEvent.EventType.MODIFY -> out.info("Event caught-File modified: " + event.path())
        DirectoryChangeEvent.EventType.DELETE -> out.info("Event caught-File deleted: " + event.path())
        DirectoryChangeEvent.EventType.OVERFLOW -> TODO()
        null -> {
          TODO()
        }
      }
    }
    // .fileHashing(false) // defaults to true
    // .logger(logger) // defaults to LoggerFactory.getLogger(DirectoryWatcher.class)
    // .watchService(watchService) // defaults based on OS to either JVM WatchService or the JNA macOS WatchService
    .build()

  @Throws(IOException::class)
  fun stopWatching() {
    watcher.close()
  }

  fun watch(): CompletableFuture<Void> {
    // you can also use watcher.watch() to block the current thread
    return watcher.watchAsync()
  }
}

fun main() = runBlocking {
  val directoryToWatch = Files.createTempDirectory("watch")
  out.info("Watching directory: $directoryToWatch")

  DirectoryWatchingUtility(directoryToWatch).watch()

  thread {
    for (i in 1..5) {
      val file = Files.createTempFile(directoryToWatch, "file-${i}-", ".txt")
      out.info("Created file: $file")
      Thread.sleep(500)
    }
  }


  delay(5500)

  out.info("End of main")
  println()
}
