package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.domain.usecases.districts.FilterDistrictsUseCase
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
class DistrictsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
) : ViewModel() {
    val districts = dataRepository.getDistricts()

    private val allDistricts = mutableListOf<District>()

    private val _uiState = MutableStateFlow(UiState<List<District>>())
    val uiState = _uiState.asStateFlow()

    init {
        loadDistricts()
    }

    fun filterDistricts(substring: String?) {
        viewModelScope.launch {
            if (substring == null) {
                _uiState.value = UiState(data = allDistricts)
            } else {
                val filteredDistricts = filterDistrictsUseCase(allDistricts, substring)
                _uiState.value = UiState(data = filteredDistricts)
            }
        }
    }

    fun loadDistricts() {
        viewModelScope.launch {
            dataRepository.getDistricts().collect { resource ->
                val error = if (resource is Resource.Error && resource.data.isNullOrEmpty()){
                    when (resource.error){
                        is IOException -> UiText.StringResource(R.string.error_network)
                        else -> UiText.StringResource(R.string.unknown_error)
                    }

                }
                else null
                _uiState.value = UiState(
                    data = resource.data,
                    isLoading = resource is Resource.Loading && resource.data.isNullOrEmpty(),
                    errorMessage = error
                )

            }
        }
    }
}
