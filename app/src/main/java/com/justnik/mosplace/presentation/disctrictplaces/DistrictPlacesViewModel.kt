package com.justnik.mosplace.presentation.disctrictplaces

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.network.PlaceTypes
import com.justnik.mosplace.di.TypePreferences
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType
import com.justnik.mosplace.domain.usecases.FilterPlacesByTypeUseCase
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictPlacesViewModel @Inject constructor(
    private val loadPlacesUseCase: LoadPlacesUseCase,
    private val filterPlacesByTypeUseCase: FilterPlacesByTypeUseCase,
    @TypePreferences private val typePreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>>
        get() = _places

    private var _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showError = MutableLiveData<Boolean>(false)
    val showError: LiveData<Boolean>
        get() = _showError

    private val allDistrictPlaces = mutableListOf<Place>()

    fun loadPlacesByDistrictId(id: Int) {
        viewModelScope.launch {
            try {
                val places = loadPlacesUseCase(id)
                _isLoading.value = false
                if (allDistrictPlaces.isEmpty()) {
                    allDistrictPlaces.addAll(places)
                }
                filterPlacesByType(getStringSelectedTypes())
            } catch (e: Exception){
                _showError.value = true
            }
        }
    }

    fun filterPlacesByType(types: List<String>) {
        val places = filterPlacesByTypeUseCase(allDistrictPlaces, types)
        _places.value = places
    }

    fun getAvailableTypes(): Array<String> {
        val uniquePlace = parsePlaceType(PlaceTypes.UNIQUE_PLACE, context)
        val restaurant = parsePlaceType(PlaceTypes.RESTAURANT, context)
        val park = parsePlaceType(PlaceTypes.PARK, context)
        return arrayOf(uniquePlace, restaurant, park)
    }

    fun getSelectedTypes(): BooleanArray {
        val types = getAvailableTypes()
        val selectedTypes = BooleanArray(types.size)
        for (i in types.indices) {
            val type = types[i]
            val defaultValue = true
            selectedTypes[i] = typePreferences.getBoolean(type, defaultValue)
        }
        return selectedTypes
    }

    private fun getStringSelectedTypes(): List<String> {
        val selectedTypes = getSelectedTypes()
        val availableTypes = getAvailableTypes()
        val stringSelectedTypes = mutableListOf<String>()
        for (i in selectedTypes.indices) {
            if (selectedTypes[i]) {
                stringSelectedTypes.add(availableTypes[i])
            }
        }
        return stringSelectedTypes
    }

}