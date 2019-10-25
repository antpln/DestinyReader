package com.android.example.destinyreader.ui.bookmarks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.BookmarkDatabase
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.BookmarksFragmentBinding
import com.android.example.destinyreader.ui.abstractList.AbstractListFragment
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.android.example.destinyreader.ui.abstractList.DestinyObjectListener
import com.android.example.destinyreader.ui.abstractList.RecordListAdapter
import com.android.example.destinyreader.ui.loreList.LoreViewModel
import kotlinx.android.synthetic.main.bookmarks_fragment.view.*

class BookmarksFragment() : AbstractListFragment() {
    companion object {
        fun newInstance() = BookmarksFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override lateinit var viewModel: AbstractListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding : BookmarksFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.bookmarks_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao
        val bookmarkDatasource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao

        val viewModelFactory = BookmarksViewModelFactory(datasource, application, bookmarkDatasource)

        viewModel = requireNotNull(
            ViewModelProviders.of(this, viewModelFactory).get(BookmarksViewModel::class.java) as? BookmarksViewModel
                ?: null)

        binding.mainViewModel = viewModel as? BookmarksViewModel ?: null

        val adapter =
            RecordListAdapter(DestinyObjectListener { id ->
                findNavController().navigate(BookmarksFragmentDirections.actionBookmarksFragmentToReaderFragment(id))
            })

        binding.bookmarksList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.bookmarks_title)

        (viewModel as? BookmarksViewModel)?.bookmarks?.observe(this, Observer{
            (viewModel as? BookmarksViewModel)?.updateItems()
        })

        binding.lifecycleOwner = this


        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }
}