package com.justnik.mosplace.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.justnik.mosplace.data.database.enteties.DistrictDbModel
import com.justnik.mosplace.data.database.enteties.PlaceDbModel
import com.justnik.mosplace.data.database.enteties.PlaceImageDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    //Districts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictDbModel>)

    @Query("SELECT * FROM districts")
    fun getAllDistricts(): Flow<List<DistrictDbModel>>

    @Query("DELETE FROM districts")
    suspend fun deleteAllDistricts()

    //Places
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(place: List<PlaceDbModel>)

    @Query("SELECT * FROM places WHERE district=:districtId")
    fun getPlacesByDistrictId(districtId: Int): Flow<List<PlaceDbModel>>

    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<PlaceDbModel>>

    @Query("DELETE FROM places WHERE district=:districtId")
    suspend fun deleteAllPlacesByDistrictId(districtId: Int)

    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()

    //PlaceImages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaceImages(placeImages: List<PlaceImageDbModel>)

    @Query("SELECT * FROM place_images WHERE place=:placeId")
    suspend fun getPlaceImagesByPlaceId(placeId: Int): List<PlaceImageDbModel>

    @Query("DELETE FROM place_images WHERE place=:placeId")
    suspend fun deleteAllPlaceImagesByPlaceId(placeId: Int)

}