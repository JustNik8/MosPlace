package com.justnik.mosplace.helpers

import android.content.Context
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.PlaceTypes

fun parsePlaceType(type: String, context: Context): String {
    val res = context.resources
    return when (type) {
        PlaceTypes.UNIQUE_PLACE -> res.getString(R.string.unique_place)
        PlaceTypes.RESTAURANT -> res.getString(R.string.restaurant)
        PlaceTypes.PARK -> res.getString(R.string.park)
        else -> ""
    }
}


fun getAbbreviationWithName(abbreviation: String, name: String) = "$abbreviation: $name"
