package me.pandelis.shush.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.pandelis.shush.R
import me.pandelis.shush.models.ChatListItem

class ChatListItemAdapter//getting the context and product list with constructor
    (//this context we will use to inflate the layout
    private val mCtx: Context, //we are storing all the products in a list
    private val chatList: List<ChatListItem>
) : RecyclerView.Adapter<ChatListItemAdapter.MessageListItemHolder>() {

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
        holder.textViewSender.text = message.sender
        holder.textViewLastMessage.text = message.lastMessage
        holder.textViewLastRecievedTime.text = message.lastMessageReceivedTime

//        holder.imageView.setImageDrawable(mCtx.resources.getDrawable(product.image))

    }



    override fun getItemCount(): Int {
        return chatList.size
    }


    inner class MessageListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewSender: TextView
        var textViewLastMessage: TextView
        var textViewLastRecievedTime: TextView
//        var imageView: ImageView

        init {
            textViewSender = itemView.findViewById(R.id.textViewTitle)
            textViewLastMessage = itemView.findViewById(R.id.textViewShortDesc)
            textViewLastRecievedTime = itemView.findViewById(R.id.textViewRating)
//            imageView = itemView.findViewById(R.id.imageView)
        }
    }
}