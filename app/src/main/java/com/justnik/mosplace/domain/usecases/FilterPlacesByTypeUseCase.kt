package com.justnik.mosplace.domain.usecases

import android.content.Context
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.parsePlaceType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilterPlacesByTypeUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(places: List<Place>, types: List<String>): List<Place> {
        return places.filter {
            val type = parsePlaceType(it.type, context)
            type in types
        }
    }
}