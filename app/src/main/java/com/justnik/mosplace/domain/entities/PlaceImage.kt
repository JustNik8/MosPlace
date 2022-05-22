package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceImage (
    val id: Int,
    val imageUrl: String,
    val place: Int
): Parcelable