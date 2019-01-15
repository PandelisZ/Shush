package me.pandelis.shush.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.pandelis.shush.R
import me.pandelis.shush.activity.ChatListActivity
import me.pandelis.shush.activity.MainActivity
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Log.e("NEW_TOKEN", s)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }


    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("fb", "empty")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        val i = Intent(this, ChatListActivity::class.java)
        val notificationId = Random().nextInt(60000)

        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = Notification.Builder(this, "M_CH_ID")
            .setContentTitle("New Message")
            .setContentText(remoteMessage?.notification?.body)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources,
                R.mipmap.ic_launcher))
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}