package me.pandelis.shush.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import me.pandelis.shush.models.DbContact
import java.util.*

@Entity(tableName = "messages")
data class MessageEntity(
    val message: String,
    val sentAt: Date,
    val receivedAt: Date,
    val contactId: Int
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}