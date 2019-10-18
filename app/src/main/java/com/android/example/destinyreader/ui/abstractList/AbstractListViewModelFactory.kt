package com.android.example.destinyreader.ui.abstractList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.destinyreader.database.DestinyDatabaseDao


abstract class AbstractListViewModelFactory (
    dataSource: DestinyDatabaseDao, application: Application, id: Long
):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    abstract override fun <T : ViewModel?> create(modelClass: Class<T>): T
}