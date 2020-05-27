package com.plin.destinyreader.ui.bookmarks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.plin.destinyreader.R
import com.plin.destinyreader.database.BookmarkDatabase
import com.plin.destinyreader.database.DestinyDatabase
import com.plin.destinyreader.databinding.BookmarksFragmentBinding
import com.plin.destinyreader.ui.abstractList.AbstractListFragment
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import com.plin.destinyreader.ui.abstractList.DestinyObjectListener
import com.plin.destinyreader.ui.abstractList.RecordListAdapter
import com.plin.destinyreader.ui.reader.Reader

class BookmarksFragment : AbstractListFragment() {
    companion object {
        fun newInstance() = BookmarksFragment()
    }

    override lateinit var viewModel: AbstractListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: BookmarksFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.bookmarks_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao
        val bookmarkDatasource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao

        val viewModelFactory =
            BookmarksViewModelFactory(datasource, application, bookmarkDatasource)

        viewModel = requireNotNull(
            ViewModelProviders.of(this, viewModelFactory)
                .get(BookmarksViewModel::class.java) as? BookmarksViewModel
        )

        binding.mainViewModel = viewModel as? BookmarksViewModel

        val adapter =
            RecordListAdapter(DestinyObjectListener { id ->
                val intent = Intent(context, Reader::class.java).apply {
                    putExtra("com.plin.destinyreader.LORE_ID", id)
                }
                startActivity(intent)
            })

        binding.bookmarksList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.bookmarks_title)

        (viewModel as? BookmarksViewModel)?.bookmarks?.observe(viewLifecycleOwner, Observer {
            (viewModel as? BookmarksViewModel)?.updateItems()
        })

        binding.lifecycleOwner = this


        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }
}