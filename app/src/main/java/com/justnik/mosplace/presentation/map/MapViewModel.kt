package com.justnik.mosplace.presentation.map

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.LoadAllPlacesUseCase

class MapViewModel : ViewModel() {
    private val repository = MosRepositoryImpl()

    suspend fun loadAllPlaces(): List<Place> {
        return loadAllPlacesUseCase().filter {
            it.latitude != 0.0 && it.longitude != 0.0
        }
    }

    private val loadAllPlacesUseCase = LoadAllPlacesUseCase(repository)
}