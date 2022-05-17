package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.R
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.mappers.DistrictMapper
import com.justnik.mosplace.data.mappers.PlaceMapper
import com.justnik.mosplace.data.network.apiservices.DataService
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.entities.District
import com.justnik.mosplace.domain.entities.Place
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val placeMapper: PlaceMapper,
    private val districtMapper: DistrictMapper,
    private val dataService: DataService
): DataRepository {

    override suspend fun loadDistricts(): Resource<List<District>> {
        return try {
            val districtsDto = dataService.getDistrictList()
            val districts = districtsDto.map { dto ->  districtMapper.dtoToEntity(dto)}
            Resource.Success(districts)
        }
        catch (e: Exception){
            Resource.Error(UiText.StringResource(R.string.unknown_error))
        }
    }

    override suspend fun loadPlacesByDistrictId(id: Int): Resource<List<Place>> {
        return try {
            val placesDto = dataService.getPlacesByDistrictId(id)
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception){
            Resource.Error(UiText.StringResource(R.string.unknown_error))
        }
    }

    override suspend fun loadAllPlaces(): Resource<List<Place>> {
        return try {
            val placesDto = dataService.getAllPlaces()
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception){
            Resource.Error(UiText.StringResource(R.string.unknown_error))
        }
    }
}