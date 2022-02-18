package com.justnik.mosplace.data.network.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class PlaceDto (
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("images")
    @Expose
    val images: List<PlaceImageDto>,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("district")
    @Expose
    val district: Int
)