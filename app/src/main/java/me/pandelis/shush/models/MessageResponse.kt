package me.pandelis.shush.models

import java.util.*

class MessageResponse(
    val payload: String,
    val sender: String,
    val createdAt: Date
)