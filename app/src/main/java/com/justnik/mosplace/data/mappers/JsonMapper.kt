package com.justnik.mosplace.data.mappers

import org.json.JSONObject
import javax.inject.Inject

class JsonMapper @Inject constructor() {
    fun jsonToString(json: JSONObject): String {
        val keys = json.keys()
        val s = StringBuilder()
        for (key in keys) {
            val jsonArr = json.getJSONArray(key)
            for (i in 0 until jsonArr.length()) {
                s.append(jsonArr[i]).append("\n")
            }
        }
        return s.toString()
    }
}