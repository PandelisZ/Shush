package me.pandelis.shush.classes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import me.pandelis.shush.models.ContactAllMessages
import me.pandelis.shush.models.DbContact
import me.pandelis.shush.models.Profile

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts")
    fun getContacts(): List<DbContact>

    @Insert
    fun add(vararg contacts: DbContact)

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun getMessagesForUser(id: Int): ContactAllMessages

}