package com.android.example.destinyreader.ui.reader

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.destinyreader.database.BookmarkDatabaseDao
import com.android.example.destinyreader.database.DestinyDatabaseDao

class ReaderViewModelFactory (
    protected val dataSource: DestinyDatabaseDao,
    protected val bookmarkDatasource : BookmarkDatabaseDao,
    protected val application: Application,
    protected val id : Long
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReaderViewModel::class.java)) {
                return ReaderViewModel(dataSource, bookmarkDatasource, application, id) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}