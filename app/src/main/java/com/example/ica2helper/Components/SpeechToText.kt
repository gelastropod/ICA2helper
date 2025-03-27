package com.example.ica2helper.Components
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SpeechToTextComposables() {
    companion object {
        @Composable
        fun minimalSTT(viewModel: SpeechToTextViewModel) {

            val spokenText by viewModel.spokenText
            val isListening by viewModel.isListening

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = spokenText, fontSize = 20.sp, modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { if (isListening) viewModel.stopListening() else viewModel.startListening() },
                    colors = ButtonDefaults.buttonColors(if (isListening) Color.Red else Color.Blue)
                ) {
                    Text(text = if (isListening) "Stop Listening" else "Start Listening")
                }
            }
        }

    }
}

class SpeechToTextHelper(private val context: Context) {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
    }

    val spokenText: MutableState<String> = mutableStateOf("")
    val isListening: MutableState<Boolean> = mutableStateOf(false)

    init {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            spokenText.value = "Speech recognition not available"
        } else {
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { isListening.value = false }
                override fun onError(error: Int) { isListening.value = false }
                override fun onResults(results: Bundle?) {
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let {
                        spokenText.value = it
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    fun startListening() {
        if (!isListening.value) {
            isListening.value = true  // ✅ Instant UI feedback
            speechRecognizer.startListening(intent)  // Call directly (no coroutine)
        }
    }

    fun stopListening() {
        isListening.value = false  // ✅ Instant UI feedback
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }
}

class SpeechToTextViewModel(private val context: Context) : ViewModel() {
    private val speechToTextHelper = SpeechToTextHelper(context)
    val spokenText = speechToTextHelper.spokenText
    val isListening = speechToTextHelper.isListening

    fun startListening() = speechToTextHelper.startListening()
    fun stopListening() = speechToTextHelper.stopListening()

    override fun onCleared() {
        super.onCleared()
        speechToTextHelper.destroy()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SpeechToTextViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SpeechToTextViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
