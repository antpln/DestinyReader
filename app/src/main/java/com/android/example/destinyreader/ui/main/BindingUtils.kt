package com.android.example.destinyreader.ui.main

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject
import com.android.example.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode

@BindingAdapter("bookTitleTextView")
fun TextView.getTitle(item: JSONDestinyObject) {
    text = item.displayProperties.name
}

fun ImageView.getIcon(item: JSONDestinyObject) {
    //TODO : Implement icon system here
    // this.setImageDrawable(item.displayProperties.icon)
}
