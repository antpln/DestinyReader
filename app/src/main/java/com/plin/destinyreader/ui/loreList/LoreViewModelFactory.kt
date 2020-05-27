package com.plin.destinyreader.ui.loreList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plin.destinyreader.database.DestinyDatabaseDao

open class LoreViewModelFactory(
    protected val dataSource: DestinyDatabaseDao,
    protected val application: Application,
    protected val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoreViewModel::class.java)) {
            return LoreViewModel(dataSource, application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}