package com.android.example.destinyreader.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.MainFragmentBinding
import com.android.example.destinyreader.ui.abstractList.AbstractListFragment
import com.android.example.destinyreader.ui.abstractList.AbstractListViewModel
import com.android.example.destinyreader.ui.abstractList.PresentationNodeListAdapter
import com.android.example.destinyreader.ui.abstractList.PresentationNodeListener

open class MainFragment : AbstractListFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override lateinit var viewModel: AbstractListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding : MainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao


        val viewModelFactory = MainViewModelFactory(datasource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel

        val adapter =
            PresentationNodeListAdapter(PresentationNodeListener { id ->
                Log.i("destinyreader", "Item clicked !")
                this.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToBooksFragment(id))
            })
        binding.bookList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        (activity as AppCompatActivity)?.supportActionBar?.title = "DestinyReader"
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.bookmarks_button -> {findNavController().navigate(MainFragmentDirections.actionMainFragmentToBookmarksFragment())}

        }



        return super.onOptionsItemSelected(item)
    }
}
