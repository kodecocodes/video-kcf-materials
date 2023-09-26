/*
 * Copyright (c) 2023 Kodeco Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.kodeco.android.kotlincoroutinesfundamentals.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun ScopesScreen(
  onNavigateBack: () -> Unit,
) {

  suspend fun navigateBackDelayed() {
    Log.d("ScopesScreen", "starting 2 seconds delay")
    delay(2.seconds)
    Log.d("ScopesScreen", "navigating back")
    onNavigateBack()
  }

  val scope = rememberCoroutineScope()
  val errorHandlingScope = CoroutineScope(
    SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
      Log.e("BuildersScreen", "Exception handler", throwable)
    })

  Column {
    Button(onClick = {
      scope.launch {
        navigateBackDelayed()
      }
      errorHandlingScope.cancel()
    }) {
      Text(text = "Navigate back in composable scope")
    }

    Button(onClick = {
      errorHandlingScope.launch {
        startNestedCoroutines()
      }
    }) {
      Text(text = "Launch coroutines in nested scopes")
    }
  }
}

private suspend fun startNestedCoroutines() {
  supervisorScope {
    launch {
      delay(2.seconds)
      throw Exception("Exception from child #1")
    }
    launch {
      delay(3.seconds)
      Log.d("ScopesScreen", "End of child #2")
    }
  }
  Log.d("ScopesScreen", "End of parent")
}
