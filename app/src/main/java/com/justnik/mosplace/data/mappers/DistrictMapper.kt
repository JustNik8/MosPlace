package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.domain.entities.District
import javax.inject.Inject

class DistrictMapper @Inject constructor(){
    fun dtoToEntity(dto: DistrictDto): District {
        return District(
            id = dto.id,
            title = dto.title,
            abbreviation = dto.abbreviation,
            imageUrl = dto.imageUrl
        )
    }
}