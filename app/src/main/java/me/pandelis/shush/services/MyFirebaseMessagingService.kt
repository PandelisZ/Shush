package me.pandelis.shush.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.pandelis.shush.R
import me.pandelis.shush.activity.ChatListActivity
import me.pandelis.shush.activity.MainActivity
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.classes.MyProfile
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.entities.MessageEntity
import me.pandelis.shush.models.DbContact
import me.pandelis.shush.models.GetMessage
import me.pandelis.shush.models.UpdateProfile
import java.util.*
import android.support.v4.content.LocalBroadcastManager
import me.pandelis.shush.activity.MessageHistoryActivity
import me.pandelis.shush.models.Contact


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Log.e("NEW_TOKEN", s)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }


    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", Context.MODE_PRIVATE).getString("fb", "empty")
    }



    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        Log.e("NEW MESSAGE", "Recieved a message")

        val DB = AppDatabase.getInstance(this)
        val API = ShushAPI.getInstance()

        val downloadedMessages = API?.messages(GetMessage(MyProfile.getInstance(DB!!)!!.publicKey))?.execute()

        val messageResonse = downloadedMessages?.body()

        messageResonse?.forEach { m ->
            var contactId = DB?.contactDao()?.getContactByPublicKey(m.sender)?.id

            if (contactId == null) {
                val newContact = API?.user(UpdateProfile(name = null, publicKey = m.sender, firebaseId = null))?.execute()
                if (newContact!!.isSuccessful) {
                    val resContact = newContact.body()!!
                    contactId = DB?.contactDao()?.add(DbContact(resContact.name, m.sender, null))?.toInt()
                }
            }

            if (contactId !== null) {
                DB?.messageDao()?.add(MessageEntity(m.payload, m.createdAt, Date(), contactId!!))
            }
            API?.deleteMessage(m.id)?.execute()
        }

        val newMessageIntent = Intent("NewMessage")
        newMessageIntent.putExtra("sender", remoteMessage?.data?.get("sender"))
        newMessageIntent.putExtra("payload", remoteMessage?.data?.get("payload"))
        broadcaster?.sendBroadcast(newMessageIntent)

        val i = Intent(this, MessageHistoryActivity::class.java)
        val contact = DB?.contactDao()?.getContactByPublicKey(remoteMessage?.data?.get("sender")!!)
        i.putExtra("contactId", contact!!.id.toString())
        i.putExtra("contactName", contact!!.name)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val notificationId = Random().nextInt(60000)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Message Notifications"
                val descriptionText = "Notifications for shush replies"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("chatNotifications", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system

                notificationManager.createNotificationChannel(channel)
            }

        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = Notification.Builder(this, "chatNotifications")
            .setContentTitle(contact!!.name)
            .setContentText(remoteMessage?.data?.get("payload"))
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources,
                R.drawable.ic_stat_name))
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}