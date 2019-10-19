package com.android.example.destinyreader.ui.loreList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.BooksFragmentBinding
import com.android.example.destinyreader.databinding.LoreFragmentBinding
import com.android.example.destinyreader.ui.abstractList.*
import com.android.example.destinyreader.ui.bookList.BooksFragmentArgs
import com.android.example.destinyreader.ui.bookList.BooksViewModel
import com.android.example.destinyreader.ui.bookList.BooksViewModelFactory

class LoreFragment() : AbstractListFragment() {
    companion object {
        fun newInstance() = LoreFragment()
    }

    override  lateinit var viewModel: AbstractListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : LoreFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.lore_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao

        val args = LoreFragmentArgs.fromBundle(requireNotNull(arguments))


        val viewModelFactory = LoreViewModelFactory(datasource, application, args.id)

        viewModel = requireNotNull(
            ViewModelProviders.of(this, viewModelFactory).get(LoreViewModel::class.java) as? LoreViewModel
                ?: null)

        binding.mainViewModel = viewModel as? LoreViewModel ?: null

        val adapter =
            RecordListAdapter(DestinyObjectListener { id ->
                Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
                findNavController().navigate(LoreFragmentDirections.actionLoreFragmentToReaderFragment(id))
            })

        binding.loreList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        viewModel.title.observe(this, Observer {
            activity?.actionBar?.title = it
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoreViewModel::class.java)
    }

}