package com.justnik.mosplace.presentation.disctrictplaces

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.place.FilterPlacesByTypeUseCase
import com.justnik.mosplace.domain.usecases.place.LoadPlacesUseCase
import com.justnik.mosplace.data.prefs.PlaceTypePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val loadPlacesUseCase: LoadPlacesUseCase,
    private val filterPlacesByTypeUseCase: FilterPlacesByTypeUseCase,
    private val placeTypePrefs: PlaceTypePrefs
) : ViewModel() {

    private val allDistrictPlaces = mutableListOf<Place>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun loadPlacesByDistrictId(id: Int) {
        if (allDistrictPlaces.isEmpty()) {
            viewModelScope.launch {
                _uiState.value = UiState(isLoading = true)
                when (val resource = loadPlacesUseCase(id)) {
                    is Resource.Success -> {
                        val places = resource.data
                        if (allDistrictPlaces.isEmpty()) {
                            allDistrictPlaces.addAll(places)
                        }
                        val selectedTypes = placeTypePrefs.selectedPrefsTypes
                            .filter { it.selected }
                            .map { it.typeName }
                        filterPlacesByType(selectedTypes)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState(error = UiState.Error.NetworkError())
                    }
                }
            }
        }
    }

    fun filterPlacesByType(types: List<String>) {
        viewModelScope.launch {
            val places = filterPlacesByTypeUseCase(allDistrictPlaces, types)
            _uiState.value = UiState(places = places)
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val places: List<Place> = listOf()
    ) {
        sealed class Error(@StringRes val errorResId: Int) {
            class NetworkError(errorResId: Int = R.string.error_network) : Error(errorResId)
        }
    }
}