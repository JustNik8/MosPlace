package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import javax.inject.Inject

class LoadDistrictsUseCase @Inject constructor(
    private val repository: MosRepository
) {

    suspend operator fun invoke(): List<District> {
        return repository.loadDistricts()
    }
}