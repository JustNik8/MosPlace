package com.justnik.mosplace.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.place.LoadAllPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val loadAllPlacesUseCase: LoadAllPlacesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllPlaces()
    }

    private fun loadAllPlaces() {
        viewModelScope.launch {
            when (val resource = loadAllPlacesUseCase()) {
                is Resource.Success -> {
                    _uiState.value = UiState(places = resource.data)
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError)
                }
            }
        }
    }

    data class UiState(
        val places: List<Place> = listOf(),
        val error: Error? = null
    ) {
        sealed class Error {
            object NetworkError : Error()
            object ServerError : Error()
        }
    }
}

