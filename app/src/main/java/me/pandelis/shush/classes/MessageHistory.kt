package me.pandelis.shush.classes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import me.pandelis.shush.models.Message
import me.pandelis.shush.models.Contact

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale


abstract class MessageHistory : AppCompatActivity(), MessagesListAdapter.SelectionListener,
    MessagesListAdapter.OnLoadMoreListener {

    protected val senderId = "0"
    protected val mySenderId = "1"
    protected var imageLoader: ImageLoader? = null
    protected var messagesAdapter: MessagesListAdapter<Message>? = null

    private var menu: Menu? = null
    private var selectionCount: Int = 0
    private var lastLoadedDate: Date? = null

    private val messageStringFormatter: MessagesListAdapter.Formatter<Message>
        get() = MessagesListAdapter.Formatter<Message> { message ->
            val createdAt = SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                .format(message.getCreatedAt())

            var text = message.getText()
            if (text == null) text = "[attachment]"

            String.format(
                Locale.getDefault(), "%s: %s (%s)",
                message.getUser().getName(), text, createdAt
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        imageLoader =
//                ImageLoader { imageView, url, payload ->
//                    Picasso.with(this@DemoMessagesActivity).load(url).into(imageView)
//                }
    }

    override fun onStart() {
        super.onStart()


        var message = Message("0",
            "Lol Hello",
            Contact("1", "Pandelis Zembashis"),
            Date()
            )

        messagesAdapter?.addToStart(message, true)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        this.menu = menu
//        menuInflater.inflate(R.menu.chat_actions_menu, menu)
//        onSelectionChanged(0)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
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
            messagesAdapter?.unselectAllItems()
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        Log.i("TAG", "onLoadMore: $page $totalItemsCount")
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages()
        }
    }

    override fun onSelectionChanged(count: Int) {
        this.selectionCount = count
//        menu!!.findItem(R.id.action_delete).isVisible = count > 0
//        menu!!.findItem(R.id.action_copy).isVisible = count > 0
    }

    private fun loadMessages() {

        var messages = ArrayList<Message>()

        messages.add(
            Message("0",
            "Lol Hello",
            Contact("1", "Pandelis Zembashis"),
            Date())
        )
        messages.add(
            Message("1",
                "Lol Hello",
                Contact("1", "Pandelis Zembashis"),
                Date())
        )
        messages.add(
            Message("2",
                "Lol Hello",
                Contact("1", "Pandelis Zembashis"),
                Date())
        )

        messagesAdapter?.addToEnd(messages, false)
    }

    companion object {

        private val TOTAL_MESSAGES_COUNT = 3
    }
}
