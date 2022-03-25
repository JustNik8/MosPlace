package com.justnik.mosplace.presentation.disctrictplaces

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase

class DistrictPlacesViewModel : ViewModel() {

    private val repository = MosRepositoryImpl()

    private val loadPlacesUseCase = LoadPlacesUseCase(repository)

    suspend fun loadPlacesByDistrictId(id: Int): List<Place> {
        return loadPlacesUseCase(id)
    }
}