package me.pandelis.shush.classes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import me.pandelis.shush.entities.MessageEntity
import me.pandelis.shush.entities.SentEntity
import me.pandelis.shush.models.Message
import me.pandelis.shush.models.Profile

@Dao
interface SentDao {

    @Insert
    fun add(vararg messages: SentEntity)

    @Query("SELECT * FROM sent WHERE contactId=:contactId")
    fun forUser(contactId: Int): List<SentEntity>

}