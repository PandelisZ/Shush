package me.pandelis.shush.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.content_message_list.view.*
import me.pandelis.shush.models.Product

class MessageListItemAdapter//getting the context and product list with constructor
    (//this context we will use to inflate the layout
    private val mCtx: Context, //we are storing all the products in a list
    private val productList: List<Product>
) : RecyclerView.Adapter<MessageListItemAdapter.MessageListItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListItemHolder {
        //inflating and returning our view holder
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.messageListRecyclerView, null)
        return MessageListItemHolder(view)
    }

    override fun onBindViewHolder(holder: MessageListItemHolder, position: Int) {
        //getting the product of the specified position
        val product = productList[position]

        //binding the data with the viewholder views
        holder.textViewTitle.text = product.sender
        holder.textViewShortDesc.text = product.lastMessage
        holder.textViewRating.text = product.lastMessageRecievedTime

//        holder.imageView.setImageDrawable(mCtx.resources.getDrawable(product.image))

    }


    override fun getItemCount(): Int {
        return productList.size
    }


    inner class MessageListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewTitle: TextView
        var textViewShortDesc: TextView
        var textViewRating: TextView
        var textViewPrice: TextView
        var imageView: ImageView

        init {
            textViewTitle = itemView.findViewById(R.id.textViewTitle)
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc)
            textViewRating = itemView.findViewById(R.id.textViewRating)
            textViewPrice = itemView.findViewById(R.id.textViewPrice)
            imageView = itemView.findViewById(R.id.imageView)
        }
    }
}