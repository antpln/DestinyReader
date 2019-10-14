package com.android.example.destinyreader.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="DestinyPresentationNodeDefinition")
data class DestinyPresentationNode(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?)


@Entity(tableName="DestinyRecordDefinition")
data class DestinyRecord(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?)


@Entity(tableName="DestinyLoreDefinition")
data class DestinyLore(

    @PrimaryKey(autoGenerate=true)
    val id : Long = 0L,

    @ColumnInfo(name="json", typeAffinity = ColumnInfo.BLOB)
    val json : ByteArray?)

