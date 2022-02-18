package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.Place

class LoadPlacesUseCase(private val repository: MosRepository) {
    suspend operator fun invoke(id: Int): List<Place>{
        return repository.loadPlaces(id)
    }
}