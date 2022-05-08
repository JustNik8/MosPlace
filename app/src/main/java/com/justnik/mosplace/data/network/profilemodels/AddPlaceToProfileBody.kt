package com.justnik.mosplace.data.network.profilemodels

import com.google.gson.annotations.SerializedName

data class AddPlaceToProfileBody (
    @SerializedName("profile_id")
    val profileId: Long,

    @SerializedName("place_id")
    val placeId: Long
)