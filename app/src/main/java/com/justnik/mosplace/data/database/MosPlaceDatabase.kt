package com.justnik.mosplace.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justnik.mosplace.data.database.enteties.DistrictDbModel
import com.justnik.mosplace.domain.entities.District

@Database(entities = [DistrictDbModel::class], version = 1)
abstract class MosPlaceDatabase : RoomDatabase() {
    abstract fun mosPlaceDao(): MosPlaceDao
}