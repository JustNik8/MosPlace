package com.justnik.mosplace.data.network.datamodels

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

    @SerializedName("short_description")
    @Expose
    val shortDescription: String,

    @SerializedName("full_description")
    @Expose
    val fullDescription: String,

    @SerializedName("longitude")
    @Expose
    val longitude: Double,

    @SerializedName("latitude")
    @Expose
    val latitude: Double,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("district")
    @Expose
    val district: Int
)