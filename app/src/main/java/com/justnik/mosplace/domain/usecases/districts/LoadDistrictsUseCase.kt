package com.justnik.mosplace.domain.usecases.districts

import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.DataRepository
import com.justnik.mosplace.domain.entities.District
import javax.inject.Inject

class LoadDistrictsUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): Resource<List<District>> = repository.loadDistricts()
}