package com.plin.destinyreader.ui.main

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

@BindingAdapter("bookTitleTextView")
fun TextView.getTitle(item: JSONDestinyObject) {
    text = item.displayProperties.name
}

fun ImageView.getIcon(item: JSONDestinyObject) {
    //TODO : Implement icon system here
    // this.setImageDrawable(item.displayProperties.icon)
}
