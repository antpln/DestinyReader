package com.plin.destinyreader.jsonParser.jsonPresentationNode

import com.plin.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.plin.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONPresentationNode(
    displayProperties: DisplayProperties,
    hash: Long,
    val children: Children
) : JSONDestinyObject(displayProperties, hash) {
    val hasLoreChild: Boolean = this.children.presentationNodes.isEmpty()
}

