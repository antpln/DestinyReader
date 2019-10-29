package com.android.example.destinyreader.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="DestinyPresentationNodeDefinition")
data class DestinyPresentationNode(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestinyPresentationNode

        if (id != other.id) return false
        if (json != null) {
            if (other.json == null) return false
            if (!json.contentEquals(other.json)) return false
        } else if (other.json != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (json?.contentHashCode() ?: 0)
        return result
    }
}


@Entity(tableName="DestinyRecordDefinition")
data class DestinyRecord(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestinyRecord

        if (id != other.id) return false
        if (json != null) {
            if (other.json == null) return false
            if (!json.contentEquals(other.json)) return false
        } else if (other.json != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (json?.contentHashCode() ?: 0)
        return result
    }
}


@Entity(tableName="DestinyLoreDefinition")
data class DestinyLore(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestinyLore

        if (id != other.id) return false
        if (json != null) {
            if (other.json == null) return false
            if (!json.contentEquals(other.json)) return false
        } else if (other.json != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (json?.contentHashCode() ?: 0)
        return result
    }
}

@Entity(tableName="DestinyInventoryItemDefinition")
data class DestinyInventoryItem(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestinyInventoryItem

        if (id != other.id) return false
        if (json != null) {
            if (other.json == null) return false
            if (!json.contentEquals(other.json)) return false
        } else if (other.json != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (json?.contentHashCode() ?: 0)
        return result
    }
}


