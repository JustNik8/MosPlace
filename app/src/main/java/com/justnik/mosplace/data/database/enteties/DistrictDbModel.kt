package com.justnik.mosplace.data.database.enteties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "districts")
data class DistrictDbModel (
    @PrimaryKey
    val id: Int,
    val title: String,
    val abbreviation: String,
)