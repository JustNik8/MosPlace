package com.justnik.mosplace.domain.repositories

import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getDistricts(): Flow<Resource<List<District>>>
    fun getPlacesByDistrictId(id: Int): Flow<Resource<List<Place>>>
    suspend fun getAllPlaces(): Flow<Resource<List<Place>>>
}