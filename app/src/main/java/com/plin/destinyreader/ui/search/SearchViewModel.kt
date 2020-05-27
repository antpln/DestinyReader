package com.plin.destinyreader.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.plin.destinyreader.database.DestinyDatabaseDao
import com.plin.destinyreader.jsonParser.jsonLore.JSONLore
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    dataSource: DestinyDatabaseDao,
    application: Application
) : AbstractListViewModel(application, dataSource, 0) {

    override val title = MutableLiveData<String>("Rechercher")
    override val _itemsList = MutableLiveData<List<JSONLore>>()
    override val itemsList: LiveData<List<JSONLore>>
        get() = _itemsList
    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    var string: String = ""


    override suspend fun findItemsFromDatabase(id: Long): List<JSONLore> {
        return withContext(Dispatchers.IO) {
            _showLoading.postValue(true)
            val list = database.searchLoreItem(string)
            val items: MutableList<JSONLore> = ArrayList()
            list.forEach {
                val string = String(
                    requireNotNull(it.json),
                    Charsets.UTF_8
                ).dropLast(1)
                val item = Gson().fromJson(
                    string,
                    JSONLore::class.java
                )
                if ((item.displayProperties.name != "") and (item.displayProperties.description != "")) {
/*                    if (item.displayProperties.icon == null) {
                        val inventoryJson =
                            database.searchInventoryItem(item.displayProperties.name)
                        if (inventoryJson != null) {
                            val jsonString = String(
                                requireNotNull(inventoryJson.json),
                                Charsets.UTF_8
                            ).dropLast(1)
                            val inventoryItem = Gson().fromJson(
                                jsonString,
                                JSONLore::class.java
                            )*//**//*
                            item.displayProperties.icon = inventoryItem.displayProperties.icon*//*
                        }
                    }*/

                    items.add(item)
                }
            }
            _showLoading.postValue(false)
            items
        }
    }

    fun updateList(searchString: String) {
        string = searchString
        uiScope.launch {
            _itemsList.value = findItemsFromDatabase()
        }
    }
}