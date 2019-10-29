package com.android.example.destinyreader.ui.bookList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksViewModel(dataSource: DestinyDatabaseDao,
                     application: Application,
                     val id : Long) : AbstractListViewModel(application, dataSource, id) {

    override val title = MutableLiveData<String>()
    override val _itemsList =  MutableLiveData<List<JSONPresentationNode>>()
    override val itemsList : LiveData<List<JSONPresentationNode>>
        get() = _itemsList

    override suspend fun scrapItemsFromDatabase(id: Long): List<JSONPresentationNode> {
        val ID = this.id
        return withContext(Dispatchers.IO) {
            Log.i("destinyreader","Id :" + ID.toString())
            val binaryArray = (database.getDestinyPresentationNode(hashToId(ID)) ?: database.getDestinyPresentationNode(ID))!!.json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )
            title.postValue(mainPresentationNode.displayProperties.name)
            Log.i("DATABASE", mainPresentationNode.displayProperties.name)
            var items: MutableList<JSONPresentationNode> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.presentationNodes.forEach {
                val string = String(
                    requireNotNull(database.getDestinyPresentationNode(hashToId(it.presentationNodeHash))!!.json),
                    Charsets.UTF_8
                ).dropLast(1)
                var item : JSONPresentationNode= Gson().fromJson(
                        string,
                        JSONPresentationNode::class.java
                    )
                if (item.displayProperties.name != "") {
                    if (item.displayProperties.icon == null) {
                        val childHash = item.children.records[0].recordHash
                        val childBinary = (database.getDestinyRecord(hashToId(childHash))
                            ?: database.getDestinyRecord(childHash))!!.json
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