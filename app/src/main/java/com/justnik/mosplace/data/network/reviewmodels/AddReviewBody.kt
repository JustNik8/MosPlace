package com.justnik.mosplace.data.network.reviewmodels

import com.google.gson.annotations.SerializedName

data class AddReviewBody (
    @SerializedName("created_date")
    val createdDate: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("stars")
    val stars: Int,

    @SerializedName("place")
    val placeId: Long,

    @SerializedName("profile")
    val profileId: Long
)