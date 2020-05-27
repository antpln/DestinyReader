package com.plin.destinyreader.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(night: BookmarkItem)

    @Query("SELECT * from bookmark_item_table WHERE id = :key")
    fun get(key: Long): BookmarkItem?

    @Query("SELECT * FROM bookmark_item_table")
    fun getAll(): LiveData<List<BookmarkItem>>

}