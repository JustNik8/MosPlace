package com.justnik.mosplace.data.network.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DistrictDto (
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("abbreviation")
    @Expose
    val abbreviation: String,

    @SerializedName("image")
    @Expose
    val imageUrl: String?
)
