package com.plin.destinyreader.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.plin.destinyreader.R
import com.plin.destinyreader.database.DestinyDatabase
import com.plin.destinyreader.databinding.SearchFragmentBinding
import com.plin.destinyreader.ui.abstractList.AbstractListFragment
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import com.plin.destinyreader.ui.abstractList.DestinyObjectListener
import com.plin.destinyreader.ui.abstractList.RecordListAdapter
import com.plin.destinyreader.ui.reader.Reader

class SearchFragment : AbstractListFragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    override lateinit var viewModel: AbstractListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: SearchFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao


        val viewModelFactory = SearchViewModelFactory(datasource, application)

        viewModel = requireNotNull(
            ViewModelProviders.of(this, viewModelFactory)
                .get(SearchViewModel::class.java) as? SearchViewModel
                ?: null
        )

        binding.mainViewModel = viewModel as? SearchViewModel ?: null

        val adapter =
            RecordListAdapter(DestinyObjectListener { id ->
                Log.i("destinyreader", "Click : " + id.toString())
                val intent = Intent(context, Reader::class.java).apply {
                    putExtra("com.plin.destinyreader.LORE_ID", id)
                }
                startActivity(intent)
            })

        binding.searchList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        (viewModel as? SearchViewModel)?.showLoading?.observe(this, Observer {
            if (it) {
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                binding.progressBar2.visibility = View.GONE
            }
        })

        viewModel.title.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })


        binding.searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (viewModel as SearchViewModel).updateList(p0.toString())
            }
        })

        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

}