package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review (
    val id: Long,
    val profileImageUrl: String,
    val profileName: String,
    val rating: Int,
    val date: String,
    val text: String
) : Parcelable