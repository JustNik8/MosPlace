package com.justnik.mosplace.presentation.place

import androidx.lifecycle.ViewModel
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.place.OpenPlaceInMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val openPlaceInMapUseCase: OpenPlaceInMapUseCase,
    private val userPrefs: UserPrefs
) : ViewModel() {

    fun openPlaceInMap(place: Place) = openPlaceInMapUseCase(place)

    fun isUserAuthorized(): Boolean = userPrefs.jwtAccessToken != null

}