package me.pandelis.shush.classes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import me.pandelis.shush.entities.MessageEntity
import me.pandelis.shush.models.Message
import me.pandelis.shush.models.Profile

@Dao
interface MessageDao {

    @Insert
    fun add(vararg messages: MessageEntity)

}