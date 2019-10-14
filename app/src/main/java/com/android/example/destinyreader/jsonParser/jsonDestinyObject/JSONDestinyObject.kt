package com.android.example.destinyreader.jsonParser.jsonDestinyObject


/*open class JSONDestinyObject(json : String) {
    val json_object : JSONObject = JSONObject(json)

    val displayProperties = DisplayProperties(json_object.getJSONObject("displayProperties"))
    val hash : Long = json_object.getLong("hash")

}

data class DisplayProperties(val jsonObject: JSONObject) {
    val text : String = jsonObject.getString("name")
    val icon : String = jsonObject.optString("icon")
    val description : String = jsonObject.optString("description")
}*/

open class JSONDestinyObject(
    val displayProperties : DisplayProperties,
    val hash : Long
)

class DisplayProperties(
    val name : String,
    val icon : String,
    val description : String
)
