package com.justnik.mosplace.domain

import android.content.Context
import com.justnik.mosplace.R

fun parsePlaceType(type: String, context: Context): String {
    val res = context.resources
    return when (type) {
        "unique_place" -> res.getString(R.string.unique_place)
        "restaurant" -> res.getString(R.string.restaurant)
        else -> ""
    }
}

fun getAbbreviationWithName(abbreviation: String, name: String) = "$abbreviation: $name"
