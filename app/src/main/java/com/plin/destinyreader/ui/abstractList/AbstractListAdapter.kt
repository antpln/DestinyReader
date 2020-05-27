package com.plin.destinyreader.ui.abstractList


import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.plin.destinyreader.jsonParser.jsonParser.JSONParser


abstract class AbstractListAdapter(private val clickListener: DestinyObjectListener) :
    ListAdapter<JSONDestinyObject, AbstractListAdapter.ViewHolder>(
        MainDiffCallback()
    ) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(getItem(position)!!, clickListener)
    }


    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    abstract class ViewHolder protected constructor(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: JSONDestinyObject, clickListener: DestinyObjectListener)
    }

    class MainDiffCallback : DiffUtil.ItemCallback<JSONDestinyObject>() {
        override fun areContentsTheSame(
            oldItem: JSONDestinyObject,
            newItem: JSONDestinyObject
        ): Boolean {
            return (oldItem.displayProperties.name == newItem.displayProperties.name) and (oldItem.displayProperties.description == newItem.displayProperties.description) and (oldItem.displayProperties.icon == newItem.displayProperties.icon)
        }

        override fun areItemsTheSame(
            oldItem: JSONDestinyObject,
            newItem: JSONDestinyObject
        ): Boolean {
            return oldItem.hash == newItem.hash
        }

    }


}

open class DestinyObjectListener(open val clickListener: (id: Long) -> Unit) {
    open fun onClick(jsonObject: JSONDestinyObject) {
        clickListener(JSONParser.hashToId(jsonObject.hash))
    }
}


