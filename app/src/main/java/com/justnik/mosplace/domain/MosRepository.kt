package com.justnik.mosplace.domain

import com.justnik.mosplace.domain.entities.District

interface MosRepository {
    suspend fun loadDistricts(): List<District>
}