package com.android.example.destinyreader.jsonParser.jsonRecord

import com.android.example.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONRecord(
    displayProperties: DisplayProperties,
    hash : Long,
    val loreHash : Long

) : JSONDestinyObject(displayProperties, hash)
