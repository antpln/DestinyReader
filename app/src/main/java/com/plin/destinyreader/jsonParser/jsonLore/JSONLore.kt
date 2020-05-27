package com.plin.destinyreader.jsonParser.jsonLore

import com.plin.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONLore(
    displayProperties: DisplayProperties,
    hash: Long

) : JSONDestinyObject(displayProperties, hash) {
    val text: String = this.displayProperties.description
}