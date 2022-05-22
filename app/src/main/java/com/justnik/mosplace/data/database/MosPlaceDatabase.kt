package com.justnik.mosplace.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justnik.mosplace.data.database.enteties.DistrictDbModel
import com.justnik.mosplace.data.database.enteties.PlaceDbModel
import com.justnik.mosplace.data.database.enteties.PlaceImageDbModel

@Database(
    entities = [DistrictDbModel::class, PlaceDbModel::class, PlaceImageDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class MosPlaceDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}