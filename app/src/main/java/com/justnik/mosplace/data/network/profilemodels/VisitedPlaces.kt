package com.justnik.mosplace.data.network.profilemodels

import com.google.gson.annotations.SerializedName

data class VisitedPlaces (
    @SerializedName("visited_places")
    val visitedPlaces: List<Long>
)