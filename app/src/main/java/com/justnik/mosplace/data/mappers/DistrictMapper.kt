package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.database.enteties.DistrictDbModel
import com.justnik.mosplace.data.network.datamodels.DistrictDto
import com.justnik.mosplace.domain.entities.District
import javax.inject.Inject

class DistrictMapper @Inject constructor(){

    fun dtoToDbModel(dto: DistrictDto): DistrictDbModel {
        return DistrictDbModel(
            id = dto.id,
            title = dto.title,
            abbreviation = dto.abbreviation,
            imageUrl = dto.imageUrl
        )
    }

    fun dbModelToEntity(dbModel: DistrictDbModel): District {
        return District(
            id = dbModel.id,
            title = dbModel.title,
            abbreviation = dbModel.abbreviation,
            imageUrl = dbModel.imageUrl
        )
    }
}