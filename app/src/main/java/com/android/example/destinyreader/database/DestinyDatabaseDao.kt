package com.android.example.destinyreader.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface DestinyDatabaseDao {

    @Query("SELECT * FROM DestinyPresentationNodeDefinition")
    fun getAllDestinyPresentationNodes() : LiveData<List<DestinyPresentationNode>>

    @Query("SELECT * FROM DestinyPresentationNodeDefinition WHERE id=:id")
    fun getDestinyPresentationNode(id : Long) : DestinyPresentationNode

    @Query("SELECT * FROM DestinyLoreDefinition WHERE id=:id")
    fun getDestinyLore(id : Long) : DestinyLore

    @Query("SELECT * FROM DestinyRecordDefinition WHERE id=:id")
    fun getDestinyRecord(id : Long) : DestinyRecord

}