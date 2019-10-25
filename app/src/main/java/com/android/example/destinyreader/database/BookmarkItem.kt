package com.android.example.destinyreader.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName="bookmark_item_table")
data class BookmarkItem (

    @PrimaryKey(autoGenerate = false)
    val id : Long,

    val isActive : Boolean

)