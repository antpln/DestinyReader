package com.android.example.destinyreader.ui.bookList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.BooksFragmentBinding
import com.android.example.destinyreader.ui.abstractList.*

class BooksFragment() : AbstractListFragment() {
    companion object {
        fun newInstance() = BooksFragment()
    }

    override  lateinit var viewModel: AbstractListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : BooksFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.books_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao

        val args = BooksFragmentArgs.fromBundle(requireNotNull(arguments))


        val viewModelFactory = BooksViewModelFactory(datasource, application, args.id)

        viewModel = requireNotNull(ViewModelProviders.of(this, viewModelFactory).get(BooksViewModel::class.java) as? BooksViewModel ?: null)

        binding.mainViewModel = viewModel as? BooksViewModel ?: null

        val adapter =
            PresentationNodeListAdapter(PresentationNodeListener { id ->
                findNavController().navigate(BooksFragmentDirections.actionBooksFragmentToLoreFragment(id))
                Log.i("destinyreader", "Item clicked !")
            })

        binding.bookList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BooksViewModel::class.java)
    }

}