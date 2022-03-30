package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.mappers.DistrictMapper
import com.justnik.mosplace.data.mappers.PlaceMapper
import com.justnik.mosplace.data.network.ApiService
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class MosRepositoryImpl @Inject constructor(
    private val placeMapper: PlaceMapper,
    private val districtMapper: DistrictMapper,
    private val apiService: ApiService
): MosRepository {

    override suspend fun loadDistricts(): List<District> {
        val districtsDto = apiService.getDistrictList()
        val districts = mutableListOf<District>()

        for (dto in districtsDto) {
            val district = districtMapper.dtoToEntity(dto)
            districts.add(district)
        }

        return districts
    }

    override suspend fun loadPlacesByDistrictId(id: Int): List<Place> {
        val placesDto = apiService.getPlacesByDistrictId(id)
        val places = placesDto.map {
            placeMapper.dtoToEntity(it)
        }
        return places
    }

    override suspend fun loadAllPlaces(): List<Place> {
        val placesDto = apiService.getAllPlaces()
        val places = placesDto.map {
            placeMapper.dtoToEntity(it)
        }
        return places
    }
}