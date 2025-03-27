package com.example.ica2helper.Components

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TextToSpeechComposables() {
    companion object {
        @Composable
        fun minimalTTS(viewModel: TextToSpeechViewModel, initialString : String) {
            var textToSpeak by remember { mutableStateOf(initialString) }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Text-to-Speech", fontSize = 22.sp)

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = textToSpeak,
                    onValueChange = { textToSpeak = it },
                    label = { Text("Enter text") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { viewModel.speak(textToSpeak) }, enabled = viewModel.isReady.value) {
                    Text("Speak")
                }
            }
        }
    }
}

class TextToSpeechHelper(context: Context) {
    private val textToSpeech: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            // Successfully initialized
            isReady.value = true
        } else {
            // Initialization failed
            isReady.value = false
        }
    }

    // Expose the readiness state
    val isReady: MutableState<Boolean> = mutableStateOf(false)

    init {
        textToSpeech.language = Locale.US // Set the language if necessary
    }

    fun speak(text: String) {
        if (isReady.value) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.e("TextToSpeechHelper", "TTS not initialized yet.")
        }
    }

    fun setPitch(pitch: Float) {
        textToSpeech.setPitch(pitch)
    }

    fun setSpeechRate(rate: Float) {
        textToSpeech.setSpeechRate(rate)
    }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}


class TextToSpeechViewModel(context: Context) : ViewModel() {
    private val ttsHelper = TextToSpeechHelper(context)

    // Expose isReady as a mutable state that can be observed in the composable
    val isReady = ttsHelper.isReady

    fun speak(text: String) = ttsHelper.speak(text)
    fun setPitch(pitch: Float) = ttsHelper.setPitch(pitch)
    fun setSpeechRate(rate: Float) = ttsHelper.setSpeechRate(rate)

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TextToSpeechViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TextToSpeechViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
