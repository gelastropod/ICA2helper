package com.example.ica2helper.Components

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ica2helper.R

class NotificationComposables {
    companion object {
        @Composable
        fun minimalNotification(context: Context, title: String, message: String) {
            LaunchedEffect(Unit) {
                NotificationHelper.createNotificationChannel(context)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        NotificationHelper.showNotification(
                            context,
                            title = title,
                            message = message,
                            notificationId = 1
                        )
                    }
                ) {
                    Text("Show Notification")
                }
            }
        }

        @Composable
        fun sendNotification(context: Context, title: String, message: String) {
            /**
             * Use this to send a notification
             */
            LaunchedEffect(Unit) {
                NotificationHelper.createNotificationChannel(context)
            }
            NotificationHelper.showNotification(
                context,
                title = title,
                message = message,
                notificationId = 1
            )
        }
    }
}

object NotificationHelper {
    private const val CHANNEL_ID = "notifications"
    private const val CHANNEL_NAME = "Notifications Channel"
    private const val CHANNEL_DESC = "Notifications"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(notificationId, builder.build())
            }
        }
    }
}
