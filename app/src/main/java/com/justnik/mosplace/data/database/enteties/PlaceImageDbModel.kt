package com.justnik.mosplace.data.database.enteties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_images")
data class PlaceImageDbModel (
    @PrimaryKey
    val id: Int,
    val imageUrl: String,
    val place: Int
)