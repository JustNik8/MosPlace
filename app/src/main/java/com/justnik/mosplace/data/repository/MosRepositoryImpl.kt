package com.justnik.mosplace.data.repository

import android.util.Log
import com.justnik.mosplace.data.mappers.DistrictMapper
import com.justnik.mosplace.data.mappers.PlaceMapper
import com.justnik.mosplace.data.network.ApiService
import com.justnik.mosplace.domain.MosRepository
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import retrofit2.HttpException
import javax.inject.Inject

class MosRepositoryImpl @Inject constructor(
    private val placeMapper: PlaceMapper,
    private val districtMapper: DistrictMapper,
    private val apiService: ApiService
): MosRepository {

    override suspend fun loadDistricts(): Resource<List<District>> {
        return try {
            val districtsDto = apiService.getDistrictList()
            val districts = districtsDto.map { dto ->  districtMapper.dtoToEntity(dto)}
            Resource.Success(districts)
        }
        catch (e: HttpException){
            Resource.Error("Error")
        }
    }

    override suspend fun loadPlacesByDistrictId(id: Int): Resource<List<Place>> {
        return try {
            val placesDto = apiService.getPlacesByDistrictId(id)
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception){
            Resource.Error("Error")
        }
    }

    override suspend fun loadAllPlaces(): Resource<List<Place>> {
        return try {
            val placesDto = apiService.getAllPlaces()
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception){
            Resource.Error("Error")
        }
    }
}