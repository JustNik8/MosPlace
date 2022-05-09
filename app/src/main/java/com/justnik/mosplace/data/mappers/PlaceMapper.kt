package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.datamodels.PlaceDto
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.entities.PlaceImage
import javax.inject.Inject

class PlaceMapper @Inject constructor(){

    fun dtoToEntity(dto: PlaceDto): Place {
        val placeImages = dto.images.map {
            PlaceImage(
                id = it.id,
                imageUrl = it.imageUrl,
                place = it.place
            )
        }

        return Place(
            id = dto.id,
            images =  placeImages,
            title = dto.title,
            shortDescription = dto.shortDescription,
            fullDescription = dto.fullDescription,
            longitude = dto.longitude,
            latitude = dto.latitude,
            type = dto.type,
            district = dto.district
        )
    }
}