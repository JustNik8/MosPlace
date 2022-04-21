package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class DistrictsViewModel @Inject constructor(
    private val loadDistrictsUseCase: LoadDistrictsUseCase,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
) : ViewModel() {

    private val _districts =
        MutableSharedFlow<List<District>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val districts = _districts.asSharedFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _showError = MutableSharedFlow<Boolean>()
    val showError = _showError.asSharedFlow()

    private val allDistricts = mutableListOf<District>()

    init {
        loadDistricts()
    }

    fun filterDistricts(substring: String?) {
        viewModelScope.launch {
            if (substring == null){
                _districts.emit(allDistricts)
            }
            else {
                val filteredDistricts = filterDistrictsUseCase(allDistricts, substring)
                _districts.emit(filteredDistricts)
            }
        }
    }

    private fun loadDistricts() {
        viewModelScope.launch {
            try {
                val districts = loadDistrictsUseCase()
                _isLoading.value = false

                _districts.emit(districts)
                if (allDistricts.isEmpty()) {
                    allDistricts.addAll(districts)
                }
            } catch (e: Exception) {
                _showError.emit(true)
            }
        }

    }

}