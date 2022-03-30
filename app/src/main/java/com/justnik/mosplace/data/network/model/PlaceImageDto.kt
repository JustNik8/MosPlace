package com.justnik.mosplace.data.network.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class PlaceImageDto (
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("image")
    @Expose
    var imageUrl: String,

    @SerializedName("place")
    @Expose
    var place: Int
)

