package com.android.example.destinyreader.jsonParser.jsonLore

import com.android.example.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONLore(
    displayProperties : DisplayProperties,
    hash : Long

) : JSONDestinyObject(displayProperties, hash) {
    val text : String = this.displayProperties.description
}