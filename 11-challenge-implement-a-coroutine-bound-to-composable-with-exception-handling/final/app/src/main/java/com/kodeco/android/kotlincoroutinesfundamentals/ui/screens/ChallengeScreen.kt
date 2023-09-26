package com.kodeco.android.kotlincoroutinesfundamentals.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun ChallengeScreen() {
  // Your code
  val scope = rememberCoroutineScope {
    Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
      Log.e("ChallengeScreen", "Fatal error", throwable)
      throw throwable
    }
  }

  var currentTutorial by remember { mutableStateOf<String?>("Not yet chosen") }

  Column {
    Button(onClick = {
      // Your code
      scope.launch {
        currentTutorial = try {
          FakeApi.getRandomTutorial()
        } catch (e: IOException) {
          Log.w("ChallengeScreen", "Network error", e)
          null
        }
      }
    }) {
      Text("Get random tutorial")
    }

    Text("Current tutorial: ${currentTutorial ?: "Error"}")
  }
}


object FakeApi {

  suspend fun getRandomTutorial(): String {
    delay(500.milliseconds) // Simulate network call
    return when ((0..4).random()) {
      0 -> "Kotlin Coroutines Fundamentals"
      1 -> "Animations in Jetpack Compose"
      2 -> "Flutter State Management"
      3 -> throw IOException("Network error")
      else -> throw OutOfMemoryError("Out of memory")
    }
  }

}
