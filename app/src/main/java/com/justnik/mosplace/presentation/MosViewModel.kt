package com.justnik.mosplace.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.LoadPlacesUseCase

class MosViewModel(application: Application) : AndroidViewModel(application){
    private val repository = MosRepositoryImpl()

    private val loadPlacesUseCase = LoadPlacesUseCase(repository)

    suspend fun loadDistricts(): List<District>{
        return repository.loadDistricts()
    }

    suspend fun loadPlacesByDistrictId(id: Int): List<Place> {
        return loadPlacesUseCase(id)
    }
}