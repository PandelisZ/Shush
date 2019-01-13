package me.pandelis.shush.models

import com.stfalcon.chatkit.commons.models.IUser

class Contact(
    private val _id: String,
    private val _name: String,
    private val _avatar: String? = null
) : IUser {

    override fun getId() = _id

    override fun getName() = _name

    override fun getAvatar() = _avatar
}