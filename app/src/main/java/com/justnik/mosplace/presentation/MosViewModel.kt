package com.justnik.mosplace.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.entities.District

class MosViewModel(application: Application) : AndroidViewModel(application){
    private val repository = MosRepositoryImpl()

    suspend fun loadDistricts(): List<District>{
        return repository.loadDistricts()
    }
}