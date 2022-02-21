package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceImage (
    var id: Int,
    var imageUrl: String,
    var place: Int
): Parcelable