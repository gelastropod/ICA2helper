package com.example.ica2helper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.ica2helper.Components.NotificationComposables
import com.example.ica2helper.Components.PictureInPictureComposables
import com.example.ica2helper.Components.PipHelper
import com.example.ica2helper.Components.SpeechToTextComposables
import com.example.ica2helper.Components.SpeechToTextViewModel
import com.example.ica2helper.Components.TextToSpeechComposables
import com.example.ica2helper.Components.TextToSpeechViewModel
import com.example.ica2helper.Components.readRawTextFile
import com.example.ica2helper.Views.Homepage
import com.example.ica2helper.ui.theme.ICA2helperTheme
import androidx.navigation.compose.composable
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        PipHelper.enterPipMode(this)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        // Handle UI visibility changes if needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        val sttViewModel = ViewModelProvider(this, SpeechToTextViewModel.Factory(this))[SpeechToTextViewModel::class.java]
        val ttsViewModel = ViewModelProvider(this, TextToSpeechViewModel.Factory(this))[TextToSpeechViewModel::class.java]

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ICA2helperTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "homepage") {
                    composable("homepage") { Homepage.Homepage(navController) }
                }
            }
        }
    }
}
