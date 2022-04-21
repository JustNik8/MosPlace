package com.justnik.mosplace.presentation.place

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.OpenPlaceInMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val openPlaceInMapUseCase: OpenPlaceInMapUseCase
) : ViewModel() {

    fun openPlaceInMap(place: Place) = openPlaceInMapUseCase(place)

}