package com.justnik.mosplace.domain

import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place

interface MosRepository {
    suspend fun loadDistricts(): List<District>
    suspend fun loadPlacesByDistrictId(id: Int): List<Place>
    suspend fun loadAllPlaces(): List<Place>
}