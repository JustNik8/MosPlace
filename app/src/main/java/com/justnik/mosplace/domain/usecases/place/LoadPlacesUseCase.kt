package com.justnik.mosplace.domain.usecases.place

import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.DataRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadPlacesUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(id: Int): Resource<List<Place>> =
        repository.loadPlacesByDistrictId(id)

}
