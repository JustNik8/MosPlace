package com.justnik.mosplace.data.mappers

import com.google.gson.JsonObject
import org.json.JSONObject
import javax.inject.Inject

class JsonMapper @Inject constructor() {

    fun loginJsonToMessage(json: JsonObject): String {
        val sb = StringBuilder()
        val keys = json.keySet()
        for (key in keys) {
            val string = json[key].asString
            sb.append(string).append("\n")
        }
        return sb.toString()
    }

    fun registrationJsonToMessage(json: JsonObject): String {
        val sb = StringBuilder()
        val keys = json.keySet()
        for (key in keys) {
            val string = json[key].asJsonArray.joinToString { "$it\n" }
                .replace("\"", "")
                .replace(",", "")
            sb.append(string)
        }
        return sb.toString()
    }

}