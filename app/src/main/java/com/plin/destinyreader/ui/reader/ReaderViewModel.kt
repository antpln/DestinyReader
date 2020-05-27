package com.plin.destinyreader.ui.reader

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.plin.destinyreader.database.BookmarkDatabaseDao
import com.plin.destinyreader.database.BookmarkItem
import com.plin.destinyreader.database.DestinyDatabaseDao
import com.plin.destinyreader.jsonParser.jsonLore.JSONLore
import com.plin.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.plin.destinyreader.jsonParser.jsonRecord.JSONRecord
import kotlinx.coroutines.*

class ReaderViewModel(
    val datasource: DestinyDatabaseDao,
    private val bookmarkDatasource: BookmarkDatabaseDao,
    val application: Application,
    val id: Long
) : ViewModel() {

    val iD: Long = id

    private val MY_PREFS_NAME: String = "DestinyReaderPrefs"

    val _text = MutableLiveData<String>()
    val text: LiveData<String>
        get() = _text

    val textSize = MutableLiveData<Int>(
        application.applicationContext.getSharedPreferences(
            MY_PREFS_NAME,
            MODE_PRIVATE
        ).getInt("textSize", 32)
    )

    val bookmark = MutableLiveData<BookmarkItem>()

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title


    val buttonState = Transformations.map(bookmark) {
        it.isActive
    }


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            scrapDataFromDatabase()
            initialiseBookmark()
        }
    }

    private suspend fun scrapDataFromDatabase() {
        Log.i("destinyreader", "ID :" + iD.toString())
        withContext(Dispatchers.IO) {
            val record =
                (datasource.getDestinyRecord(iD) ?: datasource.getDestinyRecord(hashToId(iD)))
            var loreId: Long = iD
            if (record != null) {
                val binaryArray = record.json
                val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
                val mainRecord: JSONRecord = Gson().fromJson(
                    string,
                    JSONRecord::class.java
                )
                loreId = mainRecord.loreHash
                Log.i("destinyreader", "LoreHash " + loreId)
                Log.i("destinyreader", "LoreHash Modeified " + hashToId(loreId))
            }
            val loreBinaryArray = (datasource.getDestinyLore(loreId) ?: datasource.getDestinyLore(
                hashToId(loreId)
            ))!!.json
            val loreString = String(requireNotNull(loreBinaryArray), Charsets.UTF_8).dropLast(1)
            val lore: JSONLore = Gson().fromJson(
                loreString,
                JSONLore::class.java
            )
            _title.postValue(lore.displayProperties.name)
            Log.i("destinyreader", "NAMEEE" + lore.displayProperties.description.take(30))
            _text.postValue(lore.displayProperties.description)
        }
    }

    private suspend fun initialiseBookmark() {

        withContext(Dispatchers.IO) {
            bookmark.postValue(bookmarkDatasource.get(iD) ?: BookmarkItem(iD, false))
        }
    }


    fun updateBookmark(bookmark: BookmarkItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                bookmarkDatasource.insert(bookmark)
            }
        }
    }
}