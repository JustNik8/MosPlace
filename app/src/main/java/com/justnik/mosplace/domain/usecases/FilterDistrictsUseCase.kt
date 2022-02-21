package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.entities.District

class FilterDistrictsUseCase {
    operator fun invoke(allDistricts: List<District>, substring: String): List<District>{
        return allDistricts.filter {
            it.title.contains(substring, true)
        }
    }
}