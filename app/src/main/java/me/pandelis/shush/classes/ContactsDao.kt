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

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun getContact(id: String): DbContact

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun getContact(id: Int): DbContact

    @Query("SELECT * FROM contacts WHERE publicKey=:key")
    fun getContactByPublicKey(key: String): DbContact

    @Insert
    fun add(contact: DbContact): Long

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun getMessagesForUser(id: Int): ContactAllMessages

}