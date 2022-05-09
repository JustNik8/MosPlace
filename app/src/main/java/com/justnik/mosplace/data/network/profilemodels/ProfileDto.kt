package com.justnik.mosplace.data.network.profilemodels

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val imageUrl: String,

    @SerializedName("visited_place")
    val visitedPlaceId: Long
)