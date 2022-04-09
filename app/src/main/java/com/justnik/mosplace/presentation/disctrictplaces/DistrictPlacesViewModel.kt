package com.justnik.mosplace.presentation.disctrictplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.FilterPlacesByTypeUseCase
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val loadPlacesUseCase: LoadPlacesUseCase,
    private val filterPlacesByTypeUseCase: FilterPlacesByTypeUseCase
) : ViewModel() {

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>>
        get() = _places

    private val allDistrictPlaces = mutableListOf<Place>()

    suspend fun loadPlacesByDistrictId(id: Int) {
        val places = loadPlacesUseCase(id)
        if(allDistrictPlaces.isEmpty()) {
            allDistrictPlaces.addAll(places)
        }
        _places.value = places
    }

    fun filterPlacesByType(types: List<String>) {
        val places = filterPlacesByTypeUseCase(allDistrictPlaces, types)
        _places.value = places
    }
}