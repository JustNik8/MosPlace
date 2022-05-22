package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.database.enteties.PlaceImageDbModel
import com.justnik.mosplace.data.network.datamodels.PlaceImageDto
import com.justnik.mosplace.domain.entities.PlaceImage
import javax.inject.Inject

class PlaceImageMapper @Inject constructor() {
    fun dtoToDbModel(dto: PlaceImageDto): PlaceImageDbModel {
        return PlaceImageDbModel(
            id = dto.id,
            imageUrl = dto.imageUrl,
            place = dto.place
        )
    }

    fun dbModelToEntity(dbModel: PlaceImageDbModel): PlaceImage {
        return PlaceImage(
            id = dbModel.id,
            imageUrl = dbModel.imageUrl,
            place = dbModel.place
        )
    }
}