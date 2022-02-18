package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.mappers.Mapper
import com.justnik.mosplace.data.network.ApiFactory
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place

class MosRepositoryImpl : MosRepository {
    private val mapper = Mapper()

    override suspend fun loadDistricts(): List<District> {
        val districtsDto = ApiFactory.apiService.getDistrictList()
        val districts = mutableListOf<District>()

        for (dto in districtsDto) {
            val district = mapper.districtMapDtoToEntity(dto)
            districts.add(district)
        }

        return districts
    }

    override suspend fun loadPlaces(id: Int): List<Place> {
        val placesDto = ApiFactory.apiService.getPlacesByDistrictId(PLACE_BASE_URL + id)
        val places = placesDto.map {
            mapper.placeMapDtoToEntity(it)
        }
        return places
    }

    companion object {
        private const val PLACE_BASE_URL =
            "https://mosplace.pythonanywhere.com/api/v1/places/district/"
    }
}