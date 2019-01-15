package me.pandelis.shush.activity


import android.os.Bundle
import android.os.Handler
import android.util.Log


import me.pandelis.shush.R

import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import me.pandelis.shush.classes.AppDatabase

import me.pandelis.shush.classes.MessageHistory
import me.pandelis.shush.classes.MyProfile
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.models.*
import me.pandelis.shush.services.ShushService
import me.pandelis.shush.utils.DbWorkerThread
import java.util.*

class MessageHistoryActivity : MessageHistory(), MessageInput.InputListener, MessageInput.AttachmentsListener,
    MessageInput.TypingListener {

    private var messagesList: MessagesList? = null
    private var API: ShushService? = null
    private var DB: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var contactDb: DbContact? = null
    private var contactId: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_history)

        if (intent.extras != null) {
            contactId = intent.extras.getString("contactId")
        }

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        DB = AppDatabase.getInstance(this)
        API = ShushAPI.getInstance()

        this.messagesList = findViewById(R.id.messagesList)
        initAdapter()
        loadMessages()

        val input = findViewById<MessageInput>(R.id.input)
        input.setInputListener(this)
        input.setTypingListener(this)
        input.setAttachmentsListener(this)
    }

    override fun onSubmit(input: CharSequence): Boolean {
        super.messagesAdapter?.addToStart(
            Message("423", input.toString(), Contact(super.senderId, "", ""), Date()), true
        )
        sendMessageToUser(input.toString())
        return true
    }

    fun sendMessageToUser(message: String) {

        val task = Runnable {
            if(contactDb == null) {
                val contact = DB?.contactDao()?.getContact(contactId)

                if(contact != null) {
                    contactDb = contact
                }
            }

            if(contactDb != null) {
                API?.send(SendMessage(MyProfile.getInstance(DB!!)!!.publicKey, contactDb!!.publicKey, message))
            }
        }
        mDbWorkerThread.postTask(task)

    }

    override fun loadMessages() {
        val task = Runnable {
            if(contactDb == null) {
                val contact = DB?.contactDao()?.getContact(contactId)

                if(contact != null) {
                    contactDb = contact
                }
            }

            if(contactDb != null) {
                val messagesResponse = API?.messages(GetMessage(MyProfile.getInstance(DB!!)!!.publicKey))?.execute()

                if (messagesResponse!!.isSuccessful) {
                    val messagesForDisplay = messagesResponse?.body()?.map {m ->
                        Message(
                            m.id.toString(),
                            m.payload,
                            Contact(
                                contactDb!!.id.toString(),
                                contactDb!!.name,
                                ""),
                            m.createdAt)
                    }

                    messagesAdapter?.addToEnd(messagesForDisplay, false)
                }
            }
        }
        mDbWorkerThread.postTask(task)
    }

    override fun onAddAttachments() {
//        super.messagesAdapter.addToStart(
//            MessagesFixtures.getImageMessage(), true
//        )
    }

    private fun initAdapter() {
        super.messagesAdapter = MessagesListAdapter(super.senderId, null)
        super.messagesAdapter?.enableSelectionMode(this)
        super.messagesAdapter?.setLoadMoreListener(this)
//        super.messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
//            MessagesListAdapter.OnMessageViewClickListener<Any> { view, message ->
//                AppUtils.showToast(
//                    this@DefaultMessagesActivity,
//                    message.getUser().getName() + " avatar click",
//                    false
//                )
//            })

        this.messagesList!!.setAdapter(super.messagesAdapter)
    }

    override fun onStartTyping() {
        Log.v("Typing listener", "Fired")
    }

    override fun onStopTyping() {
        Log.v("Typing listener", "Fired")
    }

}
