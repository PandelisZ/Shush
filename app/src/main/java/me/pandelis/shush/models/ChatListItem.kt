package me.pandelis.shush.models

class ChatListItem(
    val id: Int,
    val sender: String,
    val lastMessage: String,
    val lastMessageReceivedTime: String,
    val image: Int
)