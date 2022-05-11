package com.justnik.mosplace.data.network.datamodels

import com.google.gson.annotations.SerializedName
import com.justnik.mosplace.data.network.profilemodels.ProfileDto

data class ReviewDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("profile")
    val profile: ProfileDto,

    @SerializedName("created_date")
    val createdDate: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("stars")
    val stars: Int,

    @SerializedName("place")
    val placeId: Long

)