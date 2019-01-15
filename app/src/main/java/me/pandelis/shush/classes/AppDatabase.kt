package me.pandelis.shush.classes

import android.arch.persistence.room.*
import android.content.Context
import me.pandelis.shush.entities.MessageEntity
import me.pandelis.shush.models.DbContact
import me.pandelis.shush.models.Profile
import java.util.Date

@Database(
    entities = [
        Profile::class,
        DbContact::class,
        MessageEntity::class
    ], version = 3, exportSchema = false )
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {


    abstract fun profileDao(): ProfileDao
    abstract fun contactDao(): ContactsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "shush.db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}