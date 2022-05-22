package com.justnik.mosplace.data.repositories

import androidx.room.withTransaction
import com.justnik.mosplace.data.database.MosPlaceDatabase
import com.justnik.mosplace.data.mappers.DistrictMapper
import com.justnik.mosplace.data.mappers.PlaceMapper
import com.justnik.mosplace.data.network.apiservices.DataService
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.helpers.networkBounceResource
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val dataService: DataService,
    private val db: MosPlaceDatabase,
    private val placeMapper: PlaceMapper,
    private val districtMapper: DistrictMapper,
) : DataRepository {

    private val dataDao = db.dataDao()

//    override suspend fun getDistricts(): Resource<List<District>> {
//        return try {
//            val districtsDto = dataService.getDistrictList()
//            val districts = districtsDto.map { dto -> districtMapper.dtoToEntity(dto) }
//            Resource.Success(districts)
//        } catch (e: Exception) {
//            Resource.Error(e)
//        }
//    }

    override fun getDistricts() =
        networkBounceResource(
            query = {
                val districtsFlow = dataDao.getAllDistricts()
                districtsFlow.map { districtsDbModels ->
                    districtsDbModels.map { dbModel -> districtMapper.dbModelToEntity(dbModel) }
                }
            },
            fetch = {
                dataService.getDistrictList()
            },
            saveFetchResult = { districtsDto ->
                db.withTransaction {
                    val districtDbModels =
                        districtsDto.map { dto -> districtMapper.dtoToDbModel(dto) }
                    dataDao.deleteAllDistricts()
                    dataDao.insertDistricts(districtDbModels)
                }
            }
        )

    override suspend fun getPlacesByDistrictId(id: Int): Resource<List<Place>> {
        return try {
            val placesDto = dataService.getPlacesByDistrictId(id)
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getAllPlaces(): Resource<List<Place>> {
        return try {
            val placesDto = dataService.getAllPlaces()
            val places = placesDto.map { placeMapper.dtoToEntity(it) }
            Resource.Success(places)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}