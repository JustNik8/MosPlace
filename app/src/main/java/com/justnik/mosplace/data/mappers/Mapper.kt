package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.domain.entities.District

class Mapper {
    fun districtMapDtoToEntity(dto: DistrictDto): District {
        return District(
            id = dto.id,
            title = dto.title
        )
    }

}