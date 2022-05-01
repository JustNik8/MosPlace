package com.justnik.mosplace.domain.usecases.place

import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadAllPlacesUseCase @Inject constructor(
    private val repository: MosRepository
) {
    suspend operator fun invoke(): Resource<List<Place>> = repository.loadAllPlaces()
}