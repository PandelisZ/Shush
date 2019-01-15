package me.pandelis.shush.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "sent")
data class SentEntity(
    val message: String,
    val sentAt: Date,
    val receivedAt: Date,
    val contactId: Int
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}