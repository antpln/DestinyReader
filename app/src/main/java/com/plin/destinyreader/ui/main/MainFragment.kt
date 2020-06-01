package com.plin.destinyreader.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.plin.destinyreader.MainActivity
import com.plin.destinyreader.R
import com.plin.destinyreader.database.DestinyDatabase
import com.plin.destinyreader.databinding.MainFragmentBinding
import com.plin.destinyreader.ui.abstractList.AbstractListFragment
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import com.plin.destinyreader.ui.abstractList.PresentationNodeListAdapter
import com.plin.destinyreader.ui.abstractList.PresentationNodeListener
import com.plin.destinyreader.ui.settings.SettingsActivity

open class MainFragment : AbstractListFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override lateinit var viewModel: AbstractListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("destinyreader", "MainFragment")
        val binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
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

        viewModel.itemsList.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        (activity as AppCompatActivity).supportActionBar?.title = "DestinyReader"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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
        when (item.itemId) {
            R.id.bookmarks_button -> {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToBookmarksFragment())
            }
            R.id.update_button -> (activity as MainActivity).checkForUpdates()
            R.id.search_button -> findNavController().navigate(MainFragmentDirections.actionMainFragmentToSearchFragment())
            R.id.settings -> {
                val intent = Intent(context, SettingsActivity::class.java)
                startActivity(intent)
            }
        }



        return super.onOptionsItemSelected(item)
    }
}
