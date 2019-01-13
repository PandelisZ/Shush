package me.pandelis.shush.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import me.pandelis.shush.R
import me.pandelis.shush.activity.MessageHistoryActivity
import me.pandelis.shush.models.ChatListItem
import com.stfalcon.chatkit.commons.ImageLoader



class ChatListItemAdapter//getting the context and product list with constructor
    (//this context we will use to inflate the layout
    private val mCtx: Context, //we are storing all the products in a list
    private val chatList: List<ChatListItem>
) : RecyclerView.Adapter<ChatListItemAdapter.MessageListItemHolder>() {

    private val tag = "ClickListItemAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListItemHolder {
        //inflating and returning our view holder
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.chat_list_item, parent, false)
        return MessageListItemHolder(view)
    }

    override fun onBindViewHolder(holder: MessageListItemHolder, position: Int) {
        //getting the product of the specified position
        val message = chatList[position]

        //binding the data with the viewholder views
        holder.textViewSender.text = message.sender.name
        holder.textViewLastMessage.text = message.lastMessage
        holder.textViewLastReceivedTime.text = message.lastMessageReceivedTime

//        holder.imageView.setImageDrawable(mCtx.resources.getDrawable(product.image))


        // Click Listener
        val clickListener = View.OnClickListener {
            Log.d(tag, "onClick: clicked on: " + message.sender.name)

            val intent = Intent(mCtx, MessageHistoryActivity::class.java)
            intent.putExtra("senderId", message.sender.id)

            mCtx.startActivity(intent)
        }

        holder.parentLayout.setOnClickListener(clickListener)
    }



    override fun getItemCount(): Int {
        return chatList.size
    }


    inner class MessageListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewSender: TextView = itemView.findViewById(R.id.textViewSender)
        var textViewLastMessage: TextView = itemView.findViewById(R.id.textViewLastMessage)
        var textViewLastReceivedTime: TextView = itemView.findViewById(R.id.textViewRecievedTime)
        var parentLayout: LinearLayout = itemView.findViewById(R.id.chatListItemParentLayout)
//        var imageView: ImageView

        init {
            //            imageView = itemView.findViewById(R.id.imageView)
        }
    }
}