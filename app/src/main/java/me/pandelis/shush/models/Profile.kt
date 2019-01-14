package me.pandelis.shush.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Profile(
    @PrimaryKey
    val id: Int,
    val publicKey: String,
    val privateKey: String,
    val image: Int,
    val name: String
)