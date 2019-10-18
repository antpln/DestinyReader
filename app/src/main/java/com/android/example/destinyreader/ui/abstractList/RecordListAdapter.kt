package com.android.example.destinyreader.ui.abstractList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.example.destinyreader.R
import com.android.example.destinyreader.databinding.MainItemBinding
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.ui.abstractList.AbstractListFragment

class RecordListAdapter(clickListener: DestinyObjectListener) : AbstractListAdapter(
    clickListener
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListAdapter.ViewHolder {

        return ViewHolder.from(parent)

    }

    class ViewHolder private constructor(binding: MainItemBinding) : AbstractListAdapter.ViewHolder(binding) {


        val icon: ImageView = binding.bookIcon
        val title: TextView = binding.bookTitleTextView

        override fun bind(item: JSONDestinyObject, clickListener: DestinyObjectListener) {
            val res = itemView.context.resources

            //TODO : Changer système icônes
            icon.setImageResource(
                when (item.displayProperties.name) {
                    "La Lumière" -> R.drawable.light
                    "Les Ténèbres" -> R.drawable.darkness
                    "Crépuscule et aube" -> R.drawable.dawn
                    else -> R.drawable.cayde
                }
            )
            title.text = item.displayProperties.name

            if (binding is MainItemBinding) {
                binding.clickListener = clickListener as? RecordListener
                binding.jsonObject = item as? JSONDestinyObject
                binding.executePendingBindings()
            } else {
                throw Throwable("Invalid binding !")
            }

        }


        companion object {
            fun from(parent: ViewGroup): RecordListAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MainItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class RecordListener(override val clickListener: (id : Long) -> Unit) : DestinyObjectListener(clickListener){
    override fun onClick(jsonObject: JSONDestinyObject) = super.onClick(jsonObject)
}