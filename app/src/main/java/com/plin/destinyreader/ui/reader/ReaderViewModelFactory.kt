package com.plin.destinyreader.ui.reader

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plin.destinyreader.database.BookmarkDatabaseDao
import com.plin.destinyreader.database.DestinyDatabaseDao

class ReaderViewModelFactory(
    private val dataSource: DestinyDatabaseDao,
    private val bookmarkDatasource: BookmarkDatabaseDao,
    private val application: Application,
    private val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReaderViewModel::class.java)) {
            return ReaderViewModel(dataSource, bookmarkDatasource, application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}