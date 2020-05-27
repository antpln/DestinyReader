package com.plin.destinyreader.ui.abstractList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plin.destinyreader.database.DestinyDatabaseDao


abstract class AbstractListViewModelFactory(
    dataSource: DestinyDatabaseDao, application: Application, id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    abstract override fun <T : ViewModel?> create(modelClass: Class<T>): T
}