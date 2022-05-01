package com.justnik.mosplace.presentation.districts

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.usecases.districts.FilterDistrictsUseCase
import com.justnik.mosplace.domain.usecases.districts.LoadDistrictsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun loadDistricts(){
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when (val resource = loadDistrictsUseCase()){
                is Resource.Success -> {
                    val districts = resource.data ?: listOf()
                    _uiState.value = UiState(districts = districts)
                    if (allDistricts.isEmpty()) {
                        allDistricts.addAll(resource.data ?: listOf())
                    }
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError())
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
    sealed class Error(@StringRes val errorResId: Int){
        class NetworkError(errorResId: Int = R.string.error_network) : Error(errorResId)
    }
}