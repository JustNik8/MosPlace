package com.justnik.mosplace.domain.entities.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val userId: Long,
    val visitedPlaceId: Long
) : Parcelable