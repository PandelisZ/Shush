package me.pandelis.shush.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log


import me.pandelis.shush.R

import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter

import me.pandelis.shush.classes.MessageHistory
import me.pandelis.shush.models.Message

class MessageHistoryActivity : MessageHistory(), MessageInput.InputListener, MessageInput.AttachmentsListener,
    MessageInput.TypingListener {

    private var messagesList: MessagesList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_history)

        this.messagesList = findViewById(R.id.messagesList)
        initAdapter()
        loadMessages()

        val input = findViewById<MessageInput>(R.id.input)
        input.setInputListener(this)
        input.setTypingListener(this)
        input.setAttachmentsListener(this)
    }

    override fun onSubmit(input: CharSequence): Boolean {
//        super.messagesAdapter.addToStart(
//            MessagesFixtures.getTextMessage(input.toString()), true
//        )
        return true
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
