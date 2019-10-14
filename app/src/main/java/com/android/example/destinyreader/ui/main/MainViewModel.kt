package com.android.example.destinyreader.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.jsonParser.jsonParser.JSONParser
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainViewModel(dataSource: DestinyDatabaseDao, application: Application) : ViewModel() {


    val database = dataSource

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _booksList = MutableLiveData<List<JSONPresentationNode>>()
    val booksList: LiveData<List<JSONPresentationNode>>
        get() = _booksList

    init {
        uiScope.launch{
                 _booksList.value = scrapBooksFromDatabase()
        }
    }


     fun booksJob()  {
         uiScope.launch {
             _booksList.value = scrapBooksFromDatabase()
         }
         Log.i("destinyreader", "Books loaded :" + booksList.value?.size)
         return
    }

    suspend fun scrapBooksFromDatabase(): List<JSONPresentationNode> {
        return withContext(Dispatchers.IO) {
            val binaryArray = database.getDestinyPresentationNode(564676571).json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )

            Log.i("DATABASE", mainPresentationNode.displayProperties.name)
            var books: MutableList<JSONPresentationNode> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.presentationNodes.forEach {
                val string = String(
                    requireNotNull(database.getDestinyPresentationNode(JSONParser.hashToId(it.presentationNodeHash)).json),
                    Charsets.UTF_8
                ).dropLast(1)
                books.add(
                    Gson().fromJson(
                        string,
                        JSONPresentationNode::class.java
                    )
                )
            }
            Log.i("destinyreader", "books size" + books.size.toString())
            books
        }

    }

}
