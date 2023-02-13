package com.radenmas.cashless_payment.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
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

        val notificationManager: NotificationManager = applicationContext.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        var notificationBuilder: NotificationCompat.Builder
        var notificationChannel: NotificationChannel

        val title = message.notification?.title
        val text = message.notification?.body

//        val CHANNEL_ID = "Notification"
//
//        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val ringtone = RingtoneManager.getRingtone(this, soundUri)
//        ringtone.play()
//
//        val manager: NotificationManager =
//            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            "Notification",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        channel.enableVibration(true)
//        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        manager.createNotificationChannel(channel)
//
//        val builder: Notification.Builder = Notification.Builder(this, CHANNEL_ID)
//            .setSmallIcon( R.mipmap.ic_launcher_round)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
//            .setContentTitle(title)
//            .setContentText(text)
//            .setStyle(Notification.BigTextStyle().bigText(text))
//            .setShowWhen(true)
//            .setSound(soundUri)
//            .setAutoCancel(true)
//            .setDefaults(Notification.DEFAULT_ALL)
//            .setPriority(Notification.PRIORITY_DEFAULT)
//        NotificationManagerCompat.from(this).notify(1, builder.build())


        val channelID: String = "NoticeNormalId"
        val channelName: String = "NormalNotice"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is for new normal notification"
            }
            notificationManager.createNotificationChannel(notificationChannel)


        }
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText("this is my example to set big text in notification")
        bigTextStyle.setSummaryText("This is summary text")
        bigTextStyle.setBigContentTitle("Thi is big Content Title")
        val bigImage = BitmapFactory.decodeResource(resources, R.drawable.ic_qr_code)
        notificationBuilder = NotificationCompat.Builder(applicationContext, channelID)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
        notificationBuilder.setLargeIcon(bigImage)
        notificationBuilder.setStyle(bigTextStyle)
        notificationBuilder.setContentTitle("Normal Notice")
        notificationBuilder.setContentText("This is normal notice with latest API")
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(100, notificationBuilder.build())

    }
}