package com.justnik.mosplace.domain.usecases.place

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.DataRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadAllPlacesUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): Resource<List<Place>> = repository.loadAllPlaces()
}