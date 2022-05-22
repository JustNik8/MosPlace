package com.justnik.mosplace.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.justnik.mosplace.data.database.enteties.DistrictDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictDbModel>)

    @Query("SELECT * FROM districts")
    fun getAllDistricts(): Flow<List<DistrictDbModel>>

    @Query("DELETE FROM districts")
    suspend fun deleteAllDistricts()
}