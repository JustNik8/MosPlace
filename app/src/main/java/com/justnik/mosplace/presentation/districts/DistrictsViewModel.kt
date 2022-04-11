package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.usecases.FilterDistrictsUseCase
import com.justnik.mosplace.domain.usecases.LoadDistrictsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DistrictsViewModel @Inject constructor(
    private val loadDistrictsUseCase: LoadDistrictsUseCase,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
) : ViewModel() {

    private var _districts = MutableLiveData<List<District>>()
    val districts: LiveData<List<District>>
        get() = _districts

    private var _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var allDistricts = mutableListOf<District>()

    fun filterDistricts(substring: String) {
        _districts.value = filterDistrictsUseCase(allDistricts, substring)
    }

    suspend fun loadDistricts() {
        val districts = loadDistrictsUseCase()
        _isLoading.value = false

        _districts.value = districts
        if (allDistricts.isEmpty()) {
            allDistricts.addAll(districts)
        }

    }

    fun getAllDistrictsList() = allDistricts
}