package com.justnik.mosplace.presentation.disctrictplaces

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val loadPlacesUseCase: LoadPlacesUseCase
): ViewModel() {

    suspend fun loadPlacesByDistrictId(id: Int): List<Place> {
        return loadPlacesUseCase(id)
    }
}