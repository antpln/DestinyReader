package com.android.example.destinyreader.ui.bookList

import android.app.Application
import androidx.lifecycle.ViewModel
import com.android.example.destinyreader.database.DestinyDatabaseDao
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModelFactory
import com.android.example.destinyreader.ui.main.MainViewModel
import com.example.android.destinyreader.ui.main.MainViewModelFactory

class BooksViewModelFactory(val dataSource: DestinyDatabaseDao, val application: Application, val id: Long) :
    AbstractListViewModelFactory(dataSource, application, id) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            return BooksViewModel(dataSource, application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}