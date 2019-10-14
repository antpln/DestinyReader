package com.android.example.destinyreader.ui.main


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.destinyreader.R
import com.android.example.destinyreader.JSONParser.JSONPresentationNode.JSONPresentationNode
import com.android.example.destinyreader.JSONParser.JSONParser.JSONParser
import com.android.example.destinyreader.databinding.MainItemBinding


class MainAdapter(val clickListener: PresentationNodeListener) : ListAdapter<JSONPresentationNode, MainAdapter.ViewHolder>(MainDiffCallback()) {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(getItem(position)!!, clickListener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: MainItemBinding) : RecyclerView.ViewHolder(binding.root){
        val icon : ImageView = binding.bookIcon
        val title : TextView = binding.bookTitleTextView

        fun bind(
            item: JSONPresentationNode,
            clickListener: PresentationNodeListener
        ) {
            val res = itemView.context.resources

            //TODO : Changer système icônes
            icon.setImageResource(when(item.displayProperties.name) {
                "La Lumière" -> R.drawable.light
                "Les Ténèbres" -> R.drawable.darkness
                "Crépuscule et aube" -> R.drawable.dawn
                else -> R.drawable.cayde
            })
            title.text = item.displayProperties.name
            binding.clickListener = clickListener




        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MainItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    class MainDiffCallback : DiffUtil.ItemCallback<JSONPresentationNode>() {
        override fun areContentsTheSame(oldItem: JSONPresentationNode, newItem: JSONPresentationNode): Boolean {
            return (oldItem.displayProperties.name == newItem.displayProperties.name) and (oldItem.displayProperties.description == newItem.displayProperties.description) and (oldItem.displayProperties.icon == newItem.displayProperties.icon)
        }

        override fun areItemsTheSame(oldItem: JSONPresentationNode, newItem: JSONPresentationNode): Boolean {
            return oldItem.hash == newItem.hash
        }

    }


}

class PresentationNodeListener(val clickListener: (id : Long) -> Unit) {
    fun onClick(presentationNode : JSONPresentationNode) = clickListener(
        JSONParser.hashToId(presentationNode.hash))
}

