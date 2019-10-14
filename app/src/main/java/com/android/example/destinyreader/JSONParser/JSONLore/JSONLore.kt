package com.android.example.destinyreader.JSONParser.JSONLore

import com.android.example.destinyreader.JSONParser.JSONDestinyObject.DisplayProperties
import com.android.example.destinyreader.JSONParser.JSONDestinyObject.JSONDestinyObject

class JSONLore(
    displayProperties : DisplayProperties,
    hash : Long

) : JSONDestinyObject(displayProperties, hash) {
    val text : String = this.displayProperties.description
}