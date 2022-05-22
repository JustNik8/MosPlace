package com.justnik.mosplace.domain.usecases.place

import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class LoadPlacesUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(id: Int): Resource<List<Place>> =
        repository.getPlacesByDistrictId(id)

}
