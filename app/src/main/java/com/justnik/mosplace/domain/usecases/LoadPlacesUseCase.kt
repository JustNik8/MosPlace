package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadPlacesUseCase @Inject constructor(
    private val repository: MosRepository
) {
    suspend operator fun invoke(id: Int): List<Place> {
        return repository.loadPlacesByDistrictId(id)
    }
}
