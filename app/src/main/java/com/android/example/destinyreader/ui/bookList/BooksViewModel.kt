package com.android.example.destinyreader.ui.bookList

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.android.example.destinyreader.ui.main.MainViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksViewModel(dataSource: DestinyDatabaseDao,
                     application: Application,
                     val id : Long) : AbstractListViewModel(application, dataSource, id) {

    override val _itemsList =  MutableLiveData<List<JSONPresentationNode>>()
    override val itemsList : LiveData<List<JSONPresentationNode>>
        get() = _itemsList

    override suspend fun scrapItemsFromDatabase(id: Long): List<JSONPresentationNode> {
        val ID = this.id
        return withContext(Dispatchers.IO) {
            Log.i("destinyreader","Id :" + ID.toString())
            val binaryArray = (database.getDestinyPresentationNode(hashToId(ID)) ?: database.getDestinyPresentationNode(ID)).json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )

            Log.i("DATABASE", mainPresentationNode.displayProperties.name)
            var items: MutableList<JSONPresentationNode> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.presentationNodes.forEach {
                val string = String(
                    requireNotNull(database.getDestinyPresentationNode(JSONParser.hashToId(it.presentationNodeHash)).json),
                    Charsets.UTF_8
                ).dropLast(1)
                items.add(
                    Gson().fromJson(
                        string,
                        JSONPresentationNode::class.java
                    )
                )
            }
            Log.i("destinyreader", "items size" + items.size.toString())
            items
        }


    }
}