package com.justnik.mosplace.presentation.disctrictplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.PlaceTypePrefs
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.domain.usecases.place.FilterPlacesByTypeUseCase
import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.presentation.helpers.UiState
import com.justnik.mosplace.presentation.helpers.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val filterPlacesByTypeUseCase: FilterPlacesByTypeUseCase,
    private val placeTypePrefs: PlaceTypePrefs,
) : ViewModel() {

    private var allDistrictPlaces = listOf<Place>()

    private val _uiState = MutableStateFlow(UiState<List<Place>>())
    val uiState = _uiState.asStateFlow()

//    fun loadPlacesByDistrictId(id: Int) {
//        if (allDistrictPlaces.isEmpty()) {
//            viewModelScope.launch {
//                _uiState.value = UiState(isLoading = true)
//                when (val resource = loadPlacesUseCase(id)) {
//                    is Resource.Success -> {
//                        val places = resource.data
//                        if (allDistrictPlaces.isEmpty()) {
//                            allDistrictPlaces.addAll(places!!)
//                        }
//                        val selectedTypes = placeTypePrefs.selectedPrefsTypes
//                            .filter { it.selected }
//                            .map { it.typeName }
//                        filterPlacesByType(selectedTypes)
//                    }
//                    is Resource.Error -> {
//                        _uiState.value = UiState(error = UiState.Error.NetworkError())
//                    }
//                }
//            }
//        }
//    }

    fun loadPlacesByDistrictId(id: Int) {
        viewModelScope.launch {
            dataRepository.getPlacesByDistrictId(id).collect { resource ->
                val isLoading = resource is Resource.Loading && resource.data.isNullOrEmpty()

                val errorMessage =
                    if (resource is Resource.Error && resource.data.isNullOrEmpty()) {
                        when (resource.error) {
                            is IOException -> UiText.StringResource(R.string.error_network)
                            else -> UiText.StringResource(R.string.unknown_error)
                        }
                    } else null

                val selectedTypes = placeTypePrefs.selectedPrefsTypes
                    .filter { it.selected }
                    .map { it.typeName }

                allDistrictPlaces =
                    filterPlacesByTypeUseCase(resource.data ?: listOf(), selectedTypes)

                _uiState.value = UiState(
                    data = allDistrictPlaces,
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }

    fun filterPlacesByType(types: List<String>) {
        viewModelScope.launch {
            val filteredPlaces = filterPlacesByTypeUseCase(allDistrictPlaces, types)
            _uiState.value = UiState(
                data = filteredPlaces,
                isLoading = uiState.value.isLoading,
                errorMessage = uiState.value.errorMessage
            )
        }
    }

}