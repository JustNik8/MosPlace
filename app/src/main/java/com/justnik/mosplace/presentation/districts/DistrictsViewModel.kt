package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.usecases.FilterDistrictsUseCase
import com.justnik.mosplace.domain.usecases.LoadDistrictsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictsViewModel @Inject constructor(
    private val loadDistrictsUseCase: LoadDistrictsUseCase,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
) : ViewModel() {

    private val _districts = MutableLiveData<List<District>>()
    val districts: LiveData<List<District>>
        get() = _districts

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showError = MutableLiveData<Boolean>(false)
    val showError: LiveData<Boolean>
        get() = _showError

    private val allDistricts = mutableListOf<District>()

    fun filterDistricts(substring: String) {
        _districts.value = filterDistrictsUseCase(allDistricts, substring)
    }

    fun loadDistricts() {
        viewModelScope.launch {
            try {
                val districts = loadDistrictsUseCase()
                _isLoading.value = false

                _districts.value = districts
                if (allDistricts.isEmpty()) {
                    allDistricts.addAll(districts)
                }
            } catch (e: Exception) {
                _showError.value = true
            }
        }

    }

    fun getAllDistrictsList() = allDistricts
}