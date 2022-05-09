package com.justnik.mosplace.presentation.place

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.prefs.ProfilePrefs
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.auth.IsUserAuthorizedUseCase
import com.justnik.mosplace.domain.usecases.place.OpenPlaceInMapUseCase
import com.justnik.mosplace.domain.usecases.profile.AddPlaceToProfileUseCase
import com.justnik.mosplace.domain.usecases.profile.LoadVisitedProfilePlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val userPrefs: UserPrefs,
    private val profilePrefs: ProfilePrefs,
    private val openPlaceInMapUseCase: OpenPlaceInMapUseCase,
    private val addPlaceToProfileUseCase: AddPlaceToProfileUseCase,
    private val isUserAuthorizedUseCase: IsUserAuthorizedUseCase,
    private val loadVisitedProfilePlacesUseCase: LoadVisitedProfilePlacesUseCase
) : ViewModel() {

    private val checkInEventChannel = Channel<CheckInEvent>()
    val checkInEvents = checkInEventChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var visitedProfilePlaces = listOf<Long>()

    private var currentPlace: Place? = null

    init {
        loadVisitedProfilePlaces()
    }

    fun setCurrentPlace(place: Place) {
        this.currentPlace = place
    }

    fun openPlaceInMap(place: Place) = openPlaceInMapUseCase(place)

    fun isUserAuthorized(): Boolean = isUserAuthorizedUseCase()

    fun checkIn(place: Place, userLocation: Location?) {
        if (uiState.value.isPlaceAlreadyVisited) {
            viewModelScope.launch {
                checkInEventChannel.send(CheckInEvent.Review)
            }
        } else {
            compareCoordinates(place, userLocation)
        }
    }

    private fun compareCoordinates(place: Place, userLocation: Location?) {
        if (userLocation == null) {
            viewModelScope.launch {
                checkInEventChannel.send(CheckInEvent.Error(UiText.StringResource(R.string.turn_on_gps)))
            }
            return
        }

        val placeLatitude = place.latitude
        val placeLongitude = place.longitude

        val userLatitude = userLocation.latitude
        val userLongitude = userLocation.longitude

        val latitudeCheck = (userLatitude - placeLatitude).absoluteValue < DELTA
        val longitudeCheck = (userLongitude - placeLongitude).absoluteValue < DELTA

        if (latitudeCheck && longitudeCheck) {
            addPlaceToProfile(place.id.toLong())
        } else {
            viewModelScope.launch {
                checkInEventChannel.send(CheckInEvent.Error(UiText.StringResource(R.string.user_location_far)))
            }
        }
    }

    private fun addPlaceToProfile(placeId: Long) {
        viewModelScope.launch {
            val jwtAccessToken = userPrefs.jwtAccessToken
            val profileId = profilePrefs.profileId
            if (jwtAccessToken != null && profileId != -1L) {
                addPlaceToProfileUseCase(jwtAccessToken, profileId, placeId)
                checkInEventChannel.send(CheckInEvent.Success)
            } else {
                checkInEventChannel.send(CheckInEvent.Error(UiText.DynamicText("Error")))
            }
        }
    }

    private fun loadVisitedProfilePlaces() {
        viewModelScope.launch {
            if (isUserAuthorized()) {
                _uiState.value = UiState(isLoading = true)
                val resource = loadVisitedProfilePlacesUseCase(profilePrefs.visitedPlaceId)
                when (resource) {
                    is Resource.Success -> {
                        visitedProfilePlaces = resource.data.visitedPlaces
                        checkPlaceAlreadyVisited(visitedProfilePlaces)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState(
                            error = UiState.Error.NetworkError(UiText.DynamicText("error"))
                        )
                    }
                }
            }
        }
    }

    private fun checkPlaceAlreadyVisited(visitedPlaces: List<Long>) {
        val place = currentPlace
            ?: throw RuntimeException("You should set current place via method setCurrentPlace(place: Place)")
        val index = visitedPlaces.binarySearch(place.id.toLong())
        if (index >= 0) {
            _uiState.value = UiState(isPlaceAlreadyVisited = true)
        } else {
            _uiState.value = UiState(isPlaceAlreadyVisited = false)
        }


    }

    sealed class CheckInEvent {
        object Success : CheckInEvent()
        class Error(val errorMessage: UiText) : CheckInEvent()
        object Review : CheckInEvent()
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val isPlaceAlreadyVisited: Boolean = false
    ) {
        sealed class Error {
            class NetworkError(val uiText: UiText) : Error()
        }
    }

    companion object {
        private const val DELTA: Double = 0.0015
    }
}