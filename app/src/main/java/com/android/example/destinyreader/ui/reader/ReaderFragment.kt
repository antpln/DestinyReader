package com.android.example.destinyreader.ui.reader

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.ReaderFragmentBinding

class ReaderFragment() : Fragment() {
    companion object {
        fun newInstance() = ReaderFragment()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    lateinit var viewModel : ReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : ReaderFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.reader_fragment, container, false)
        val application : Application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao
        val args = ReaderFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = ReaderViewModelFactory(datasource, application, args.id)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReaderViewModel::class.java)

        binding.readerViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }
}