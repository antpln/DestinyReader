package com.plin.destinyreader.jsonParser.jsonRecord

import com.plin.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONRecord(
    displayProperties: DisplayProperties,
    hash: Long,
    val loreHash: Long

) : JSONDestinyObject(displayProperties, hash)
