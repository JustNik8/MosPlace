package com.justnik.mosplace.presentation.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.prefs.ProfilePrefs
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.place.OpenPlaceInMapUseCase
import com.justnik.mosplace.domain.usecases.profile.AddPlaceToProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val userPrefs: UserPrefs,
    private val profilePrefs: ProfilePrefs,
    private val openPlaceInMapUseCase: OpenPlaceInMapUseCase,
    private val addPlaceToProfileUseCase: AddPlaceToProfileUseCase
) : ViewModel() {

    private val checkInEventChannel = Channel<CheckInEvent>()
    val checkInEvents = checkInEventChannel.receiveAsFlow()

    fun openPlaceInMap(place: Place) = openPlaceInMapUseCase(place)

    fun isUserAuthorized(): Boolean = userPrefs.jwtAccessToken != null

    fun addPlaceToProfile(placeId: Long) {
        viewModelScope.launch {
            val jwtAccessToken = userPrefs.jwtAccessToken
            val profileId = profilePrefs.profileId
            if (jwtAccessToken != null && profileId != -1L) {
                addPlaceToProfileUseCase(jwtAccessToken, profileId, placeId)
                checkInEventChannel.send(CheckInEvent.Success(UiText.DynamicText("Success")))
            } else {
                checkInEventChannel.send(CheckInEvent.Error(UiText.DynamicText("Error")))
            }
        }
    }

    sealed class CheckInEvent {
        class Success(val successMessage: UiText) : CheckInEvent()
        class Error(val errorMessage: UiText) : CheckInEvent()
    }
}