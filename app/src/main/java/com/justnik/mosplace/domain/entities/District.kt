package com.justnik.mosplace.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class District (
    val id: Int,
    val title: String,
    val abbreviation: String,
    val imageUrl: String?
) : Parcelable