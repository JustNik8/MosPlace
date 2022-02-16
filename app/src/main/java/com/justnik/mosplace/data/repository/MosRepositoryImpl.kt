package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.mappers.Mapper
import com.justnik.mosplace.data.network.ApiFactory
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District

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
}