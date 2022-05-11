package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.justnik.mosplace.data.network.profilemodels.ProfileDto
import com.justnik.mosplace.domain.entities.profile.Profile
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review (
    val id: Long,
    val profile: Profile,
    val createdDate: String,
    val text: String,
    val stars: Int,
    val placeId: Long
) : Parcelable