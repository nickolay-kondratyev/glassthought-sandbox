package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*
import kotlin.random.Random

// Simulating a View interface for displaying data
interface View {
  fun showNews(news: List<String>)
  fun showUser(user: String)
  fun showProgressBar()
  fun hideProgressBar()
}

val out = Out.standard()

// A simple implementation of the View interface
class ConsoleView : View {
  override fun showNews(news: List<String>) {
    out.println("News: $news")
  }

  override fun showUser(user: String) {
    out.println("User: $user")
  }

  override fun showProgressBar() {
    out.println("Loading...")
  }

  override fun hideProgressBar() {
    out.println("Done loading.")
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

  scope.launch {
    updateNews(view)
  }

  scope.launch {
    updateProfile(view)
  }

  // Keep the program alive long enough to see the output
  delay(2000L)
}
