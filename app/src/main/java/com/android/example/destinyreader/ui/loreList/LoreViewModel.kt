package com.android.example.destinyreader.ui.loreList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoreViewModel(dataSource: DestinyDatabaseDao,
                     application: Application,
                     val id : Long) : AbstractListViewModel(application, dataSource, id) {

    override val _itemsList =  MutableLiveData<List<JSONRecord>>()
    override val itemsList : LiveData<List<JSONRecord>>
        get() = _itemsList

    override suspend fun scrapItemsFromDatabase(id: Long): List<JSONRecord> {
        val ID = this.id
        return withContext(Dispatchers.IO) {
            Log.i("destinyreader", "ID "+ ID.toString())
            Log.i("destinyreader", "Transformed ID "+ hashToId(ID).toString())
            val binaryArray = database.getDestinyPresentationNode(ID).json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )

            Log.i("destinyreader", "Loaded " + mainPresentationNode.displayProperties.name)
            var items: MutableList<JSONRecord> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.records.forEach {
                val string = String(
                    requireNotNull(database.getDestinyRecord(hashToId(it.recordHash)).json),
                    Charsets.UTF_8
                ).dropLast(1)
                val item =  Gson().fromJson(
                    string,
                    JSONRecord::class.java
                )
                if (item.displayProperties.name != mainPresentationNode.displayProperties.name) {
                    items.add(item)
                }
            }
            Log.i("destinyreader", "items size" + items.size.toString())
            items
        }


    }
}