package com.justnik.mosplace.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justnik.mosplace.data.database.enteties.DistrictDbModel

@Database(entities = [DistrictDbModel::class], version = 1)
abstract class MosPlaceDatabase : RoomDatabase() {
    abstract fun mosPlaceDao(): MosPlaceDao
}