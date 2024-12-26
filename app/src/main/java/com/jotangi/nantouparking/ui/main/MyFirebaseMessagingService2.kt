package com.jotangi.nantouparking.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import com.jotangi.nantouparking.R

class MyFirebaseMessagingService2 : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMReceive", "New FCM token: $token")
        // Send the token to your server or store it locally
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received from: ${remoteMessage.from}")

        // Extract the title and body from the notification or data payload
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Default Title"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Default Body"

        // Log the payload for debugging
        remoteMessage.data.let { data ->
            Log.d("FCM", "Message data payload: $data")
        }
        remoteMessage.notification?.let {
            Log.d("FCM", "Message notification body: ${it.body}")
        }

        // Display the notification directly in the app
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build and display the notification
        val notification = NotificationCompat.Builder(this, channelId)
            // Use your app's notification icon
            .setSmallIcon(R.drawable.ic_notification)

            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Automatically removes the notification when clicked
            .build()

        notificationManager.notify(0, notification)
    }
}
