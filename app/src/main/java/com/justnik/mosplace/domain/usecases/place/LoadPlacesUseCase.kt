package com.justnik.mosplace.domain.usecases.place

import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadPlacesUseCase @Inject constructor(
    private val repository: MosRepository
) {
    suspend operator fun invoke(id: Int): Resource<List<Place>> =
        repository.loadPlacesByDistrictId(id)

}
