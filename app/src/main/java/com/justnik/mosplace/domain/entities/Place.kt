package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place (
    val id: Int,
    val images: List<PlaceImage>,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val longitude: Double,
    val latitude: Double,
    val type: String,
    val district: Int
): Parcelable