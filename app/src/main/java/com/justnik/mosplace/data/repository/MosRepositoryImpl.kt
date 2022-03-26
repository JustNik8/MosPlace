package com.justnik.mosplace.data.repository

import com.google.android.gms.common.api.Api
import com.justnik.mosplace.data.mappers.Mapper
import com.justnik.mosplace.data.network.ApiFactory
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class MosRepositoryImpl @Inject constructor(
    private val mapper: Mapper
): MosRepository {

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
        val placesDto = ApiFactory.apiService.getPlacesByDistrictId(id)
        val places = placesDto.map {
            mapper.placeMapDtoToEntity(it)
        }
        return places
    }

    override suspend fun loadAllPlaces(): List<Place> {
        val placesDto = ApiFactory.apiService.getAllPlaces()
        val places = placesDto.map {
            mapper.placeMapDtoToEntity(it)
        }
        return places
    }


}