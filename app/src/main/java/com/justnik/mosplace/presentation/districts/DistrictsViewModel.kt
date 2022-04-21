package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.usecases.FilterDistrictsUseCase
import com.justnik.mosplace.domain.usecases.LoadDistrictsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class DistrictsViewModel @Inject constructor(
    private val loadDistrictsUseCase: LoadDistrictsUseCase,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
) : ViewModel() {

    private val allDistricts = mutableListOf<District>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDistricts()
    }

    fun filterDistricts(substring: String?) {
        viewModelScope.launch {
            if (substring == null) {
                _uiState.value = UiState(districts = allDistricts)
            } else {
                val filteredDistricts = filterDistrictsUseCase(allDistricts, substring)
                _uiState.value = UiState(districts = filteredDistricts)
            }
        }
    }

    private fun loadDistricts(){
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when (val resource = loadDistrictsUseCase()){
                is Resource.Success -> {
                    _uiState.value = UiState(districts = resource.data ?: listOf())
                    allDistricts.addAll(resource.data ?: listOf())
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError)
                }
            }
        }
    }

}

data class UiState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val districts: List<District> = listOf()
) {
    sealed class Error{
        object NetworkError : Error()
        object ServerError : Error()
    }
}