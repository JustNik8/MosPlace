package com.justnik.mosplace.presentation.place

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.OpenPlaceInMapUseCase

class PlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val openPlaceInMapUseCase = OpenPlaceInMapUseCase(application)

    fun openPlaceInMap(place: Place) {
        openPlaceInMapUseCase(place)
    }
}