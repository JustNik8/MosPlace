package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.data.network.model.PlaceDto
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.entities.PlaceImage
import com.yandex.mapkit.geometry.Point
import javax.inject.Inject

class Mapper @Inject constructor(){
    fun districtMapDtoToEntity(dto: DistrictDto): District {
        return District(
            id = dto.id,
            title = dto.title,
            abbreviation = dto.abbreviation,
            imageUrl = dto.imageUrl
        )
    }

    fun placeMapDtoToEntity(dto: PlaceDto): Place {
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

    fun mapPlaceToPoint(place: Place): Point{
        return Point(place.latitude, place.longitude)
    }
}