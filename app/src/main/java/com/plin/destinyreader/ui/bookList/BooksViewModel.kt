package com.plin.destinyreader.ui.bookList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.plin.destinyreader.database.DestinyDatabaseDao
import com.plin.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.plin.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.plin.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksViewModel(
    dataSource: DestinyDatabaseDao,
    application: Application,
    val id: Long
) : AbstractListViewModel(application, dataSource, id) {

    override val title = MutableLiveData<String>()
    override val _itemsList = MutableLiveData<List<JSONPresentationNode>>()
    override val itemsList: LiveData<List<JSONPresentationNode>>
        get() = _itemsList

    override suspend fun findItemsFromDatabase(id: Long): List<JSONPresentationNode> {
        val iD = this.id
        return withContext(Dispatchers.IO) {
            Log.i("destinyreader", "Id :" + iD.toString())
            val binaryArray = (database.getDestinyPresentationNode(iD))!!.json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )
            title.postValue(mainPresentationNode.displayProperties.name)
            Log.i("DATABASE", mainPresentationNode.displayProperties.name)
            val items: MutableList<JSONPresentationNode> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.presentationNodes.forEach {
                val hash = it.presentationNodeHash
                Log.i(
                    "destinyreader",
                    it.presentationNodeHash.toString() + " hash : " + hashToId(it.presentationNodeHash)
                )

                val string = String(
                    requireNotNull(database.getDestinyPresentationNode(hashToId(hash))!!.json),
                    Charsets.UTF_8
                ).dropLast(1)
                val item: JSONPresentationNode = Gson().fromJson(
                    string,
                    JSONPresentationNode::class.java
                )
                if (item.displayProperties.name != "") {
                    if (item.displayProperties.icon == null) {
                        val childHash = item.children.records[0].recordHash
                        val childBinary = database.getDestinyRecord(hashToId(childHash))!!.json
                        val childString =
                            String(requireNotNull(childBinary), Charsets.UTF_8).dropLast(1)

                        val child = Gson().fromJson(
                            childString,
                            JSONRecord::class.java
                        )

                        item.displayProperties.icon = child.displayProperties.icon
                    }
                    items.add(item)
                }
            }
            Log.i("destinyreader", "items size" + items.size.toString())
            items
        }


    }

}
