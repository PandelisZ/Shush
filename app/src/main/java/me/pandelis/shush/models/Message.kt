package me.pandelis.shush.models

import com.stfalcon.chatkit.commons.models.IMessage
import java.util.*


class Message(
    private val _id: String,
    private val text: String,
    private val sender: Contact,
    private val sentAt: Date
) : IMessage {

    override fun getId(): String {
        return _id
    }

    override fun getText(): String {
        return text
    }

    override fun getUser(): Contact {
        return sender
    }

    override fun getCreatedAt(): Date {
        return sentAt
    }
}