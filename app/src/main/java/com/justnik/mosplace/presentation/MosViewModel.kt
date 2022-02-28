package com.justnik.mosplace.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.*

class MosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MosRepositoryImpl()

    private val loadPlacesUseCase = LoadPlacesUseCase(repository)
    private val loadDistrictsUseCase = LoadDistrictsUseCase(repository)
    private val filterDistrictsUseCase = FilterDistrictsUseCase()
    private val openPlaceInMapUseCase = OpenPlaceInMapUseCase(application)
    private val loadAllPlacesUseCase = LoadAllPlacesUseCase(repository)

    private var _districts = MutableLiveData<List<District>>()
    val districts: LiveData<List<District>>
        get() = _districts

    suspend fun loadDistricts(): List<District> {
        return loadDistrictsUseCase()
    }

    suspend fun loadPlacesByDistrictId(id: Int): List<Place> {
        return loadPlacesUseCase(id)
    }

    fun filterDistricts(allDistricts: List<District>, substring: String) {
        _districts.value = filterDistrictsUseCase(allDistricts, substring)
    }

    fun openPlaceInMap(place: Place) {
        openPlaceInMapUseCase(place)
    }

    suspend fun loadAllPlaces(): List<Place> {
        return loadAllPlacesUseCase().filter {
            it.latitude != 0.0 && it.longitude != 0.0
        }
    }
}