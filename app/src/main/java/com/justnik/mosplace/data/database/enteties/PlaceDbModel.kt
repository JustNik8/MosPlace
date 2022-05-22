package com.justnik.mosplace.data.database.enteties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceDbModel (
    @PrimaryKey
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val longitude: Double,
    val latitude: Double,
    val type: String,
    val district: Int
)