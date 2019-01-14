package me.pandelis.shush.classes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import me.pandelis.shush.models.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM Profile LIMIT 1")
    fun getProfile(): Profile?

    @Insert
    fun createProfile(vararg profiles: Profile)

}