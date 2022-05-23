package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.database.enteties.PlaceDbModel
import com.justnik.mosplace.data.database.enteties.PlaceImageDbModel
import com.justnik.mosplace.data.network.datamodels.PlaceDto
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.entities.PlaceImage
import javax.inject.Inject

class PlaceMapper @Inject constructor(
    private val placeImageMapper: PlaceImageMapper
) {

    fun dtoToDbModel(dto: PlaceDto): PlaceDbModel {
        return PlaceDbModel(
            id = dto.id,
            title = dto.title,
            shortDescription = dto.shortDescription,
            fullDescription = dto.fullDescription,
            longitude = dto.longitude,
            latitude = dto.latitude,
            type = dto.type,
            district = dto.district
        )
    }

    fun dbModelToEntity(dbModel: PlaceDbModel, imageDbModels: List<PlaceImageDbModel>): Place {
        val placeImages =
            imageDbModels.map { imageDbModel -> placeImageMapper.dbModelToEntity(imageDbModel) }

        return Place(
            id = dbModel.id,
            images = placeImages,
            title = dbModel.title,
            shortDescription = dbModel.shortDescription,
            fullDescription = dbModel.fullDescription,
            longitude = dbModel.longitude,
            latitude = dbModel.latitude,
            type = dbModel.type,
            district = dbModel.district
        )
    }
}