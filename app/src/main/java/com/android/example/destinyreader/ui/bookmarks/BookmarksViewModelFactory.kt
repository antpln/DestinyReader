package com.android.example.destinyreader.ui.bookmarks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.destinyreader.database.BookmarkDatabaseDao
import com.android.example.destinyreader.database.DestinyDatabaseDao

open class BookmarksViewModelFactory(
    protected val dataSource: DestinyDatabaseDao,
    protected val application: Application,
    protected val bookmarksDatasource : BookmarkDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarksViewModel::class.java)) {
            return BookmarksViewModel(dataSource, application, bookmarksDatasource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}