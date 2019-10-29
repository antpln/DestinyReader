package com.android.example.destinyreader.ui.reader

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.example.destinyreader.database.BookmarkDatabaseDao
import com.android.example.destinyreader.database.BookmarkItem
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonLore.JSONLore
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser.Companion.hashToId
import com.android.example.destinyreader.jsonParser.jsonRecord.JSONRecord
import com.google.gson.Gson
import kotlinx.coroutines.*

class ReaderViewModel(val datasource:DestinyDatabaseDao, val bookmarkDatasource : BookmarkDatabaseDao, val application:Application, val id:Long) : ViewModel() {

    val ID : Long = id

    val MY_PREFS_NAME: String = "DestinyReaderPrefs"

    val _text = MutableLiveData<String>()
    val text : LiveData<String>
        get() = _text

    val textSize = MutableLiveData<Int>(application.applicationContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getInt("textSize", 32))

    val bookmark = MutableLiveData<BookmarkItem>()

    private val _title = MutableLiveData<String>()
    val title : LiveData<String>
        get() = _title


    val buttonState = Transformations.map(bookmark, {
        it.isActive
    })


    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            scrapDataFromDatabase()
            initialiseBookmark()
        }
    }

    suspend fun scrapDataFromDatabase() {
        withContext(Dispatchers.IO) {
            val binaryArray = (datasource.getDestinyRecord(ID) ?: datasource.getDestinyRecord(hashToId(ID)))!!.json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainRecord : JSONRecord = Gson().fromJson(
                string,
                JSONRecord::class.java
            )
            _title.postValue(mainRecord.displayProperties.name)

            val loreId : Long = mainRecord.loreHash
            Log.i("destinyreader","LoreHash " + loreId)
            Log.i("destinyreader","LoreHash Modeified " + hashToId(loreId))
            val loreBinaryArray = (datasource.getDestinyLore(loreId) ?: datasource.getDestinyLore(hashToId(loreId)))!!.json
            val loreString = String(requireNotNull(loreBinaryArray), Charsets.UTF_8).dropLast(1)
            val lore : JSONLore = Gson().fromJson(
                loreString,
                JSONLore::class.java
            )
            Log.i("destinyreader", "NAMEEE" + lore.displayProperties.description.take(30))
            _text.postValue(lore.displayProperties.description)
        }
    }

    private suspend fun initialiseBookmark(){

        withContext(Dispatchers.IO) {
            bookmark.postValue(bookmarkDatasource.get(ID)?:BookmarkItem(ID, false))
        }
    }


     fun updateBookmark(bookmark : BookmarkItem) {
         uiScope.launch {
             withContext(Dispatchers.IO) {
                 bookmarkDatasource.insert(bookmark)
             }
         }
     }
}