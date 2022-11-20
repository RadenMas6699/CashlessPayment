package com.radenmas.cashless_payment.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.radenmas.cashless_payment.R

/**
 * Created by RadenMas on 01/04/2022.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val text = message.notification?.body

        val CHANNEL_ID = "Notification"

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(this, soundUri)
        ringtone.play()

        val manager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Notif",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)

        val builder: Notification.Builder = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_scan_outline)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_scan_outline))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(Notification.BigTextStyle().bigText(text))
            .setShowWhen(true)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(Notification.PRIORITY_HIGH)
        NotificationManagerCompat.from(this).notify(1, builder.build())

    }
}