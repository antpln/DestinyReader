package com.plin.destinyreader.ui.bookmarks

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.plin.destinyreader.database.BookmarkDatabaseDao
import com.plin.destinyreader.database.BookmarkItem
import com.plin.destinyreader.database.DestinyDatabaseDao
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.plin.destinyreader.jsonParser.jsonLore.JSONLore
import com.plin.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.plin.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel(

    val dataSource: DestinyDatabaseDao,
    val application: Application,
    bookmarksDatasource: BookmarkDatabaseDao
) : AbstractListViewModel(application, dataSource, 0) {

    override val title = MutableLiveData<String>()
    override val _itemsList = MutableLiveData<List<JSONDestinyObject>>()
    override val itemsList: LiveData<List<JSONDestinyObject>>
        get() = _itemsList


    private val bookmarksFromDatabase = bookmarksDatasource.getAll()

    val bookmarks: LiveData<List<BookmarkItem>> = Transformations.map(bookmarksFromDatabase) {
        it.filter {
            it.isActive
        }
    }

    fun updateItems() = uiScope.launch {
        _itemsList.postValue(findItemsFromDatabase())
    }

    init {
        updateItems()
    }


    override suspend fun findItemsFromDatabase(id: Long): List<JSONDestinyObject> {
        return withContext(Dispatchers.IO) {
            val list: MutableList<JSONDestinyObject> = ArrayList()
            bookmarks.value?.forEach {
                var binaryArray =
                    (dataSource.getDestinyRecord(it.id)
                        ?: dataSource.getDestinyRecord(hashToId(it.id)))?.json
                if (binaryArray != null) {
                    var string = String(binaryArray, Charsets.UTF_8)
                    string = string.dropLast(1)
                    val record = Gson().fromJson(
                        string,
                        JSONRecord::class.java
                    )
                    Log.i("destinyreader", "BOOKMARK : " + record.displayProperties.name)
                    list.add(record)
                } else {
                    binaryArray = (dataSource.getDestinyLore(it.id)
                        ?: dataSource.getDestinyLore(hashToId(it.id)))!!.json
                    var string = String(requireNotNull(binaryArray), Charsets.UTF_8)
                    string = string.dropLast(1)
                    val lore = Gson().fromJson(
                        string,
                        JSONLore::class.java
                    )
                    Log.i("destinyreader", "BOOKMARK : " + lore.displayProperties.name)
                    list.add(lore)
                }

            }
            list
        }
    }


}