package com.justnik.mosplace.presentation.disctrictplaces

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.PlaceTypes
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.di.TypePreferences
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType
import com.justnik.mosplace.domain.usecases.FilterPlacesByTypeUseCase
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val loadPlacesUseCase: LoadPlacesUseCase,
    private val filterPlacesByTypeUseCase: FilterPlacesByTypeUseCase,
    @TypePreferences private val typePreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val allDistrictPlaces = mutableListOf<Place>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun loadPlacesByDistrictId(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when (val resource = loadPlacesUseCase(id)) {
                is Resource.Success -> {
                    val places = resource.data ?: listOf()
                    _uiState.value = UiState(places = places)
                    if (allDistrictPlaces.isEmpty()) {
                        allDistrictPlaces.addAll(places)
                    }
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError())
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

    fun getAvailableTypes(): Array<String> {
        val uniquePlace = parsePlaceType(PlaceTypes.UNIQUE_PLACE, context)
        val restaurant = parsePlaceType(PlaceTypes.RESTAURANT, context)
        val park = parsePlaceType(PlaceTypes.PARK, context)
        return arrayOf(uniquePlace, restaurant, park)
    }

    fun getSelectedTypes(): BooleanArray {
        val types = getAvailableTypes()
        val selectedTypes = BooleanArray(types.size)
        for (i in types.indices) {
            val type = types[i]
            val defaultValue = true
            selectedTypes[i] = typePreferences.getBoolean(type, defaultValue)
        }
        return selectedTypes
    }

    private fun getStringSelectedTypes(): List<String> {
        val selectedTypes = getSelectedTypes()
        val availableTypes = getAvailableTypes()
        val stringSelectedTypes = mutableListOf<String>()
        for (i in selectedTypes.indices) {
            if (selectedTypes[i]) {
                stringSelectedTypes.add(availableTypes[i])
            }
        }
        return stringSelectedTypes
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val places: List<Place> = listOf()
) {
    sealed class Error(@StringRes val errorResId: Int){
        class NetworkError(errorResId: Int = R.string.error_network) : Error(errorResId)
    }
}