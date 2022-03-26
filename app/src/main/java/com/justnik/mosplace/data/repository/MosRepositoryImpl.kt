package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.mappers.Mapper
import com.justnik.mosplace.data.network.ApiService
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class MosRepositoryImpl @Inject constructor(
    private val mapper: Mapper,
    private val apiService: ApiService
): MosRepository {

    override suspend fun loadDistricts(): List<District> {
        val districtsDto = apiService.getDistrictList()
        val districts = mutableListOf<District>()

        for (dto in districtsDto) {
            val district = mapper.districtMapDtoToEntity(dto)
            districts.add(district)
        }

        return districts
    }

    override suspend fun loadPlaces(id: Int): List<Place> {
        val placesDto = apiService.getPlacesByDistrictId(id)
        val places = placesDto.map {
            mapper.placeMapDtoToEntity(it)
        }
        return places
    }

    override suspend fun loadAllPlaces(): List<Place> {
        val placesDto = apiService.getAllPlaces()
        val places = placesDto.map {
            mapper.placeMapDtoToEntity(it)
        }
        return places
    }


}