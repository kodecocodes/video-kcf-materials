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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun JobsScreen() {
  val coroutineScope = rememberCoroutineScope {
    Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
      Log.d("JobsScreen", "Caught in CoroutineExceptionHandler", throwable)
    }
  }

  val parentJob = Job()
  Column {
    Button(onClick = {
      Log.d("JobsScreen", "Coroutine about to start. Job: ${parentJob.dumpState()}")
      coroutineScope.launch(parentJob) {
        Log.d("JobsScreen", "Coroutine started. Job: ${parentJob.dumpState()}")
        try {
          delay(2.seconds)
        } catch (e: Exception) {
          Log.d("JobsScreen", "Delay failed", e)
          if (e is CancellationException) {
            throw e
          }
        }

        Log.d("JobsScreen", "Coroutine finished. Job: ${parentJob.dumpState()}")
      }
    }) {
      Text(text = "Launch coroutine")
    }

    Button(onClick = {
      Log.d("JobsScreen", "Job about to cancel. Job: ${parentJob.dumpState()}")
      parentJob.cancel()
      Log.d("JobsScreen", "Job cancellation requested. Job: ${parentJob.dumpState()}")
    }) {
      Text(text = "Cancel coroutine")
    }
  }
}

private fun Job.dumpState() = "active: $isActive, cancelled: $isCancelled, completed: $isCompleted"
