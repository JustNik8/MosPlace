package com.justnik.mosplace.presentation.map

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.LoadAllPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val loadAllPlacesUseCase: LoadAllPlacesUseCase
) : ViewModel() {

    suspend fun loadAllPlaces(): List<Place> {
        return loadAllPlacesUseCase().filter {
            it.latitude != 0.0 && it.longitude != 0.0
        }
    }
}
