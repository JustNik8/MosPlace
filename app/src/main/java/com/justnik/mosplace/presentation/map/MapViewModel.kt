package com.justnik.mosplace.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.repositories.DataRepository
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
class MapViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<List<Place>>())
    val uiState = _uiState.asStateFlow()

    init {
        getAllPlaces()
    }

    private fun getAllPlaces(){
        viewModelScope.launch {
            dataRepository.getAllPlaces().collect { resource ->
                val isLoading = resource is Resource.Loading && resource.data.isNullOrEmpty()

                val errorMessage = if (resource is Resource.Error && resource.data.isNullOrEmpty()){
                    when (resource.error){
                        is IOException -> UiText.StringResource(R.string.error_network)
                        else -> UiText.StringResource(R.string.unknown_error)
                    }
                }
                else null

                _uiState.value = UiState(
                    data = resource.data,
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }
}

