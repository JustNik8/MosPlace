package com.justnik.mosplace.domain.usecases.districts

import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import javax.inject.Inject

class LoadDistrictsUseCase @Inject constructor(
    private val repository: MosRepository
) {
    suspend operator fun invoke(): Resource<List<District>> = repository.loadDistricts()
}