package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.data.network.ApiFactory
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District

class LoadDistrictsUseCase(private val repository: MosRepository) {

    suspend operator fun invoke(): List<District>{
        return repository.loadDistricts()
    }
}