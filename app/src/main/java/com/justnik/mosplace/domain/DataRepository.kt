package com.justnik.mosplace.domain

import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place

interface DataRepository {
    suspend fun loadDistricts(): Resource<List<District>>
    suspend fun loadPlacesByDistrictId(id: Int): Resource<List<Place>>
    suspend fun loadAllPlaces(): Resource<List<Place>>
}