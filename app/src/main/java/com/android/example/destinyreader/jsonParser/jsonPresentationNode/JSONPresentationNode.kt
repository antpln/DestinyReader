package com.android.example.destinyreader.jsonParser.jsonPresentationNode

import com.android.example.destinyreader.jsonParser.jsonDestinyObject.DisplayProperties
import com.android.example.destinyreader.jsonParser.jsonDestinyObject.JSONDestinyObject

class JSONPresentationNode(
    displayProperties: DisplayProperties,
    hash : Long,
    val originalIcon: String,
    val rootViewIcon: String,
    val nodeType: String,
    val scope: String,
    val objectiveHash: Long,
    val children: Children
) : JSONDestinyObject(displayProperties, hash) {
    val hasLoreChild : Boolean = this.children.presentationNodes.isEmpty()
}

