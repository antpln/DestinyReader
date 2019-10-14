package com.android.example.destinyreader.ui.main

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
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.MainFragmentBinding
import com.example.android.destinyreader.ui.main.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : MainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao


        val viewModelFactory = MainViewModelFactory(datasource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = viewModel

        val adapter = MainAdapter(PresentationNodeListener{
            id -> Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
            Log.i("destinyreader", "Item clicked !")
        })
        binding.bookList.adapter = adapter

        adapter.submitList(viewModel.booksList.value)

        viewModel.booksList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
