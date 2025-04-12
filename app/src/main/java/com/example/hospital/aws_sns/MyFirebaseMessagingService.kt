package com.example.hospital.aws_sns

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hospital.R
import com.example.hospital.core.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")

        try {
            // Check if we have this nested structure
            if (remoteMessage.data.containsKey("default")) {
                val defaultJson = remoteMessage.data["default"]
                val jsonObject = JSONObject(defaultJson)

                // Extract the GCM message
                if (jsonObject.has("GCM")) {
                    val gcmMessage = jsonObject.getString("GCM")
                    val gcmJson = JSONObject(gcmMessage)

                    // Extract notification data
                    if (gcmJson.has("notification")) {
                        val notification = gcmJson.getJSONObject("notification")
                        val title = notification.getString("title")
                        val body = notification.getString("body")

                        Log.d(TAG, "Extracted notification - Title: $title, Body: $body")

                        // Show the notification
                        sendNotification(title, body)
                    }

                    // You can also handle data payload similarly
                }
            } else if (remoteMessage.notification != null) {
                // Handle standard FCM notification payload
                val title = remoteMessage.notification?.title ?: "Notification"
                val body = remoteMessage.notification?.body ?: ""
                sendNotification(title, body)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing message payload", e)
        }
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this app's subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }



    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data["default"]
        if (data != null) {
            try {
                val outerJson = JSONObject(data)
                val gcmJson = outerJson.optString("GCM")
                val gcm = JSONObject(gcmJson)
                val notification = gcm.optJSONObject("notification")
                val title = notification?.optString("title") ?: "Title"
                val body = notification?.optString("body") ?: "Body"

                Log.d(TAG, "Extracted notification - Title: $title, Body: $body")

                sendNotification(title, body)
            } catch (e: JSONException) {
                Log.e(TAG, "Failed to parse GCM payload", e)
            }
        }
    }


    /**
     * Send the FCM token to your app server.
     */
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your backend
        // This would typically involve an API call to your server

        // For example:
        // ApiClient.getInstance().registerDevice(token, userId);
    }

    /**
     * Create and show a notification containing the received FCM message.
     */
    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "default_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // ✅ MUST have this
            .setContentTitle(title ?: "Notification")
            .setContentText(messageBody ?: "")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 🚀 ensures it pops
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH // 🚨 ensure heads-up
            )
            channel.description = "Channel for general push notifications"
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}