package com.justnik.mosplace.domain.usecases

import android.content.Context
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilterPlacesByTypeUseCase @Inject constructor(
    @ApplicationContext private val context: Context
){
    operator fun invoke(places: List<Place>, types: List<String>): List<Place>{
        val result = mutableListOf<Place>()
        for (place in places){
            val type = parsePlaceType(place.type, context)
            if (type in types){
                result.add(place)
            }
        }
        return result
    }
}