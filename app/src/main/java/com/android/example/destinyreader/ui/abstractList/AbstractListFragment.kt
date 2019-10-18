package com.android.example.destinyreader.ui.abstractList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.destinyreader.R
import com.android.example.destinyreader.database.DestinyDatabase
import com.android.example.destinyreader.databinding.BooksFragmentBinding
import com.android.example.destinyreader.databinding.MainFragmentBinding
import com.android.example.destinyreader.ui.bookList.BooksFragment
import com.android.example.destinyreader.ui.bookList.BooksFragmentArgs
import com.android.example.destinyreader.ui.bookList.BooksViewModel
import com.android.example.destinyreader.ui.bookList.BooksViewModelFactory
import com.android.example.destinyreader.ui.main.*
import com.android.example.destinyreader.ui.main.MainFragment
import com.example.android.destinyreader.ui.main.MainViewModelFactory

abstract class AbstractListFragment : Fragment() {



    abstract var viewModel: AbstractListViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}