package com.plin.destinyreader.ui.loreList

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
import com.plin.destinyreader.database.DestinyDatabase
import com.plin.destinyreader.databinding.LoreFragmentBinding
import com.plin.destinyreader.ui.abstractList.AbstractListFragment
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import com.plin.destinyreader.ui.abstractList.DestinyObjectListener
import com.plin.destinyreader.ui.abstractList.RecordListAdapter
import com.plin.destinyreader.ui.reader.Reader

class LoreFragment : AbstractListFragment() {
    companion object {
        fun newInstance() = LoreFragment()
    }

    override lateinit var viewModel: AbstractListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: LoreFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.lore_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = DestinyDatabase.getInstance(application).destinyDatabaseDao

        val args = LoreFragmentArgs.fromBundle(requireNotNull(arguments))


        val viewModelFactory = LoreViewModelFactory(datasource, application, args.id)

        viewModel = requireNotNull(
            ViewModelProviders.of(this, viewModelFactory)
                .get(LoreViewModel::class.java) as? LoreViewModel
                ?: null
        )

        binding.mainViewModel = viewModel as? LoreViewModel ?: null

        val adapter =
            RecordListAdapter(DestinyObjectListener { id ->
                val intent = Intent(context, Reader::class.java).apply {
                    putExtra("com.plin.destinyreader.LORE_ID", id)
                }
                startActivity(intent)
            })

        binding.loreList.adapter = adapter

        adapter.submitList(viewModel.itemsList.value)

        viewModel.itemsList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        viewModel.title.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })

        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoreViewModel::class.java)
    }

}