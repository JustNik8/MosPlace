package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class District (
    val id: Int,
    val title: String
) : Parcelable