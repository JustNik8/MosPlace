package com.justnik.mosplace.presentation.districts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.usecases.FilterDistrictsUseCase
import com.justnik.mosplace.domain.usecases.LoadDistrictsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DistrictsViewModel @Inject constructor(
    private val loadDistrictsUseCase: LoadDistrictsUseCase,
    private val filterDistrictsUseCase: FilterDistrictsUseCase
): ViewModel() {

    private var _districts = MutableLiveData<List<District>>()
    val districts: LiveData<List<District>>
        get() = _districts

    fun filterDistricts(allDistricts: List<District>, substring: String) {
        _districts.value = filterDistrictsUseCase(allDistricts, substring)
    }

    suspend fun loadDistricts(): List<District> {
        return loadDistrictsUseCase()
    }
}