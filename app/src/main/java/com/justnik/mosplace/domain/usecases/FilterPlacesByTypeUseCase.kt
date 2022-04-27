package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class FilterPlacesByTypeUseCase @Inject constructor() {
    operator fun invoke(places: List<Place>, types: List<String>): List<Place> {
        return places.filter {
            val type = it.type
            type in types
        }
    }
}