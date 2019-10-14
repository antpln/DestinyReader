package com.android.example.destinyreader.JSONParser.JSONRecord

import com.android.example.destinyreader.JSONParser.JSONDestinyObject.DisplayProperties
import com.android.example.destinyreader.JSONParser.JSONDestinyObject.JSONDestinyObject

class JSONRecord(
    displayProperties: DisplayProperties,
    hash : Long,
    loreHash : Long

) : JSONDestinyObject(displayProperties, hash)
