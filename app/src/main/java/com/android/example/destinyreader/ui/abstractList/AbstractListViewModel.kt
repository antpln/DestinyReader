package com.android.example.destinyreader.ui.abstractList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class AbstractListViewModel(application : Application, dataSource : DestinyDatabaseDao, id:Long) : ViewModel(){

    val database = dataSource

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    abstract val title : MutableLiveData<String>

    abstract val _itemsList : MutableLiveData<out List<JSONDestinyObject>>
    open val itemsList : LiveData<out List<JSONDestinyObject>>
        get() = _itemsList

    init {
        uiScope.launch{
            _itemsList.value = scrapItemsFromDatabase()
        }
    }


    fun itemsJob()  {
        uiScope.launch {
            _itemsList.value = scrapItemsFromDatabase()
        }
        Log.i("destinyreader", "Items loaded :" + itemsList.value?.size)
        return
    }
    abstract suspend fun scrapItemsFromDatabase(id : Long = 0) : List<JSONDestinyObject>
}