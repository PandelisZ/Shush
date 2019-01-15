package me.pandelis.shush.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "contacts")
data class DbContact(
    val name: String,
    val publicKey: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Profile

        if (id != other.id) return false
        if (!Arrays.equals(image, other.image)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + Arrays.hashCode(image)
        result = 31 * result + name.hashCode()
        return result
    }
}