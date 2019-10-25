package com.android.example.destinyreader.ui.reader

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.BookmarkDatabase
import com.android.example.destinyreader.database.BookmarkItem
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.ReaderFragmentBinding


class ReaderFragment() : Fragment() {


    val MY_PREFS_NAME: String = "DestinyReaderPrefs"

    companion object {
        fun newInstance() = ReaderFragment()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    lateinit var viewModel : ReaderViewModel
    lateinit var sharedPrefs : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var bookmarkState : MutableLiveData<Boolean>
    lateinit var buttonIcon : LiveData<Int>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : ReaderFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.reader_fragment, container, false)
        val application : Application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao
        val bookmarkDatasource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao
        val args = ReaderFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = ReaderViewModelFactory(datasource, bookmarkDatasource,application, args.id)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReaderViewModel::class.java)

        viewModel.title.observe(this, Observer {
            (activity as AppCompatActivity)?.supportActionBar?.title = it
        })

        binding.readerViewModel = viewModel

        sharedPrefs = context!!.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        editor = sharedPrefs.edit()

        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        val readerTextSize : LiveData<String> = Transformations.map(viewModel.textSize, {
                it -> it.toString() + "sp"
        })




        return binding.root

    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.reader_menu, menu)
        val optionsMenu = menu
        val buttonIcon = Transformations.map(viewModel.buttonState, {
            when(it) {
                true -> R.drawable.sharp_bookmark_white_24
                else -> R.drawable.sharp_bookmark_border_white_24
            }
        })
        buttonIcon.observe(this, Observer {
            optionsMenu.findItem(R.id.bookmark_button).setIcon(it)
            Log.i("destinyreader", "ICON CHANGED : " + it)
        })

        viewModel.bookmark.observe(this, Observer{
            it -> Log.i("destinyreader", "BOOKMARK ID : " + it.id + " ACTIVE :" + it.isActive)
        })


        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.text_size_plus -> {
                viewModel.textSize.value = viewModel.textSize.value?.plus(1);
                editor.putInt("textSize", requireNotNull(viewModel.textSize.value))
                editor.commit()
                return true;
            }
            R.id.text_size_minus-> {
                viewModel.textSize.value = viewModel.textSize.value?.minus(1);
                editor.putInt("textSize", requireNotNull(viewModel.textSize.value))
                editor.commit()
                return true;
            }

            R.id.bookmark_button -> {
                    viewModel.bookmark.value = BookmarkItem(viewModel.ID, !(viewModel.bookmark.value!!.isActive))
                    viewModel.updateBookmark(requireNotNull(viewModel.bookmark?.value))
                    return true
            }

            else -> {return false}
        }
    }

}