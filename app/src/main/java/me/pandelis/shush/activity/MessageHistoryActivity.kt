package me.pandelis.shush.activity

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.stfalcon.chatkit.commons.ImageLoader
import me.pandelis.shush.R
import com.stfalcon.chatkit.messages.MessagesListAdapter
import me.pandelis.shush.models.Message
import java.util.*


abstract class MessageHistoryActivity : AppCompatActivity(), MessagesListAdapter.SelectionListener,
    MessagesListAdapter.OnLoadMoreListener {

    protected val tag = "MessageHistoryActivity"
    private val imageLoader = null
    protected val senderId = "0"
    private var messagesAdapter: MessagesListAdapter<Message>? = null

    private var menu: Menu? = null
    private var selectionCount: Int = 0
    private var lastLoadedDate: Date? = null

    private val messageStringFormatter: MessagesListAdapter.Formatter<Message>
        get() = MessagesListAdapter.Formatter { message ->
            val createdAt = SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                .format(message.createdAt)

            var text = message.text
            if (text == null) text = "[attachment]"

            String.format(
                Locale.getDefault(), "%s: %s (%s)",
                message.user.name, text, createdAt
            )
        }
    override fun onStart() {
        super.onStart()
//        messagesAdapter!!.addToStart(MessagesFixtures.getTextMessage(), true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
//        menuInflater.inflate(R.menu.chat_actions_menu, menu)
        onSelectionChanged(0)
        return true
    }

//    fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            R.id.action_delete -> messagesAdapter!!.deleteSelectedMessages()
//            R.id.action_copy -> {
//                messagesAdapter!!.copySelectedMessagesText(this, messageStringFormatter, true)
//                AppUtils.showToast(this, R.string.copied_message, true)
//            }
//        }
//        return true
//    }

    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter!!.unselectAllItems()
        }
    }

//    override fun onLoadMore(page: Int, totalItemsCount: Int) {
//        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
//            loadMessages()
//        }
//    }

//    override fun onSelectionChanged(count: Int) {
//        this.selectionCount = count
//        menu!!.findItem(R.id.action_delete).setVisible(count > 0)
//        menu!!.findItem(R.id.action_copy).setVisible(count > 0)
//    }

//    protected fun loadMessages() {
//        Handler().postDelayed(Runnable //imitation of internet connection
//        {
//            val messages = MessagesFixtures.getMessages(lastLoadedDate)
//            lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt()
//            messagesAdapter!!.addToEnd(messages, false)
//        }, 1000
//        )
//    }
//
//    companion object {
//
//        private val TOTAL_MESSAGES_COUNT = 100
//    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_message_history)
        Log.d(tag, "onCreate: started")
    }

    private fun getIncomingIntent() {
        if(intent.hasExtra("senderId")) {
            Log.d(tag, "getIncomingIntent: Found intent extras")
        }

        val senderId = intent.getStringExtra("senderId")


    }

//    private fun retrieveMessagesFromSender(senderId: String) {
//        Log.d(tag, "Retrieving messages for sender: $senderId")
//
//        val adapter = MessagesListAdapter<Message>(senderId, null)
//
//
//
//        messagesList.setAdapter(adapter)
//    }
}
