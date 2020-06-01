package com.plin.destinyreader.ui.reader

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import com.plin.destinyreader.R
import com.plin.destinyreader.database.BookmarkDatabase
import com.plin.destinyreader.database.BookmarkItem
import com.plin.destinyreader.database.DestinyDatabase
import com.plin.destinyreader.databinding.ActivityReaderBinding
import kotlinx.android.synthetic.main.activity_reader.*

class Reader : AppCompatActivity() {


    private val MY_PREFS_NAME: String = "DestinyReaderPrefs"
    private lateinit var viewModel: ReaderViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var binding: ActivityReaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reader)
        val application: Application = this.application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao
        val bookmarkDatasource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao
        val id = intent.getLongExtra("com.plin.destinyreader.LORE_ID", 0L)
        Log.i("destinyreader", "Received : " + id.toString())
        val viewModelFactory =
            ReaderViewModelFactory(datasource, bookmarkDatasource, application, id)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReaderViewModel::class.java)

        viewModel.title.observe(this, Observer {
            binding.toolbarLayout.title = it
        })

        binding.readerViewModel = viewModel

        sharedPrefs = this.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()



        binding.lifecycleOwner = this

        //setHasOptionsMenu(true)

        val readerTextSize: LiveData<String> = Transformations.map(viewModel.textSize) {
            it.toString() + "sp"
        }

        binding.bookmarkButton.setOnClickListener {
            viewModel.bookmark.value =
                BookmarkItem(viewModel.iD, !(viewModel.bookmark.value!!.isActive))
            viewModel.updateBookmark(requireNotNull(viewModel.bookmark.value))
        }


        val buttonIcon = Transformations.map(viewModel.buttonState) {
            when (it) {
                true -> R.drawable.sharp_bookmark_white_24
                else -> R.drawable.sharp_bookmark_border_white_24
            }
        }
        buttonIcon.observe(this, Observer {
            binding.bookmarkButton.setImageResource(it)
            Log.i("destinyreader", "ICON CHANGED : " + it)
        })

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}
