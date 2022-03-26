package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.getAbbreviationWithName
import javax.inject.Inject

class FilterDistrictsUseCase @Inject constructor(){
    operator fun invoke(allDistricts: List<District>, substring: String): List<District>{
        return allDistricts.filter {
            val fullName = getAbbreviationWithName(it.abbreviation, it.title)
            fullName.contains(substring, true)
        }
    }
}

