package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*
import kotlin.random.Random

// Simulating a View interface for displaying data
interface View {
  suspend fun showNews(news: List<String>)
  suspend fun showUser(user: String)
  suspend fun showProgressBar()
  suspend fun hideProgressBar()
}

val out = Out.standard()

// A simple implementation of the View interface
class ConsoleView : View {
  override suspend fun showNews(news: List<String>) {
    out.println("News: $news")
  }

  override suspend fun showUser(user: String) {
    out.println("User: $user")
  }

  override suspend fun showProgressBar() {
    out.printlnBlue("Loading...")
  }

  override suspend fun hideProgressBar() {
    out.printlnBlue("Done loading.")
  }
}

// Simulated functions to fetch data
suspend fun getNewsFromApi(): List<String> {
  delay(1000L) // Simulating network delay
  return listOf("News 1", "News 2", "News 3")
}

suspend fun getUserData(): String {
  delay(800L) // Simulating network delay

  return "John Doe"
}



// Suspending functions to update news and profile
suspend fun updateNews(view: View) {
  view.showProgressBar()
  val news = getNewsFromApi()
  val sortedNews = news.sortedByDescending { Random.nextInt(1, 100) } // Simulated sorting
  view.showNews(sortedNews)
  view.hideProgressBar()
}

suspend fun updateProfile(view: View) {
  val user = getUserData()
  view.showUser(user)
}

// Main function
fun main() = runBlocking {
  val view = ConsoleView()
  val scope = CoroutineScope(Dispatchers.Default + Job())

  scope.launch(CoroutineName("UpdateNews")) {
    updateNews(view)
  }

  scope.launch {
    updateProfile(view)
  }


  // Keep the program alive long enough to see the output
  delay(2000L)
}
