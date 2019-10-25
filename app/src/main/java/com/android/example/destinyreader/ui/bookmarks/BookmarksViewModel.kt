package com.android.example.destinyreader.ui.bookmarks

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.BookmarkDatabaseDao
import com.android.example.destinyreader.database.BookmarkItem
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.database.DestinyLore
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.android.example.destinyreader.jsonParser.jsonLore.JSONLore
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.android.example.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel(

    val dataSource : DestinyDatabaseDao,
    val application : Application,
    val bookmarksDatasource : BookmarkDatabaseDao
) : AbstractListViewModel(application, dataSource, 0){

    override val title = MutableLiveData<String>()
    override val _itemsList = MutableLiveData<List<JSONRecord>>()
    override val itemsList: LiveData<List<JSONRecord>>
        get() = _itemsList


    private val bookmarksFromDatabase = bookmarksDatasource.getAll()

    val bookmarks : LiveData<List<BookmarkItem>> = Transformations.map(bookmarksFromDatabase, {
        it.filter {
            it.isActive
        }
    } )

    fun updateItems() = uiScope.launch {
        _itemsList.postValue(scrapItemsFromDatabase())
    }

    init {
        updateItems()
    }


    override suspend fun scrapItemsFromDatabase(id : Long): List<JSONRecord> {
        return withContext(Dispatchers.IO) {
            val list: MutableList<JSONRecord> = ArrayList()
            bookmarks.value?.forEach {
                Log.i("destinyreader", "ID boef : " + it.id.toString() + " isActive : " + it.isActive.toString())
                val binaryArray =
                    (dataSource.getDestinyRecord(it.id) ?: dataSource.getDestinyRecord(hashToId(it.id)))!!.json
                var string = String(requireNotNull(binaryArray), Charsets.UTF_8)
                string = string.dropLast(1)
                val Record = Gson().fromJson(
                    string,
                    JSONRecord::class.java
                )
                Log.i("destinyreader", "BOOKMARK : " + Record.displayProperties.name)
                list.add(Record)
            }
            list
        }
    }


}