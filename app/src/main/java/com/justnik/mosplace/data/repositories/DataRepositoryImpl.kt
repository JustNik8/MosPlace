package com.justnik.mosplace.data.repositories

import androidx.room.withTransaction
import com.justnik.mosplace.data.database.MosPlaceDatabase
import com.justnik.mosplace.data.mappers.DistrictMapper
import com.justnik.mosplace.data.mappers.PlaceImageMapper
import com.justnik.mosplace.data.mappers.PlaceMapper
import com.justnik.mosplace.data.network.apiservices.DataService
import com.justnik.mosplace.data.network.datamodels.PlaceDto
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.helpers.networkBounceResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val dataService: DataService,
    private val db: MosPlaceDatabase,
    private val placeMapper: PlaceMapper,
    private val districtMapper: DistrictMapper,
    private val placeImageMapper: PlaceImageMapper
) : DataRepository {

    private val dataDao = db.dataDao()

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

    override fun getPlacesByDistrictId(id: Int): Flow<Resource<List<Place>>> =
        networkBounceResource(
            query = {
                val placesFlow = dataDao.getPlacesByDistrictId(id)
                placesFlow.map { placesDbModels ->
                    placesDbModels.map { dbModel ->
                        val imageDbModels = dataDao.getPlaceImagesByPlaceId(dbModel.id)
                        placeMapper.dbModelToEntity(dbModel, imageDbModels)
                    }
                }
            },
            fetch = {
                dataService.getPlacesByDistrictId(id)
            },
            saveFetchResult = { placesDto ->
                db.withTransaction {
                    val placeDbModels = placesDto.map { dto -> placeMapper.dtoToDbModel(dto) }

                    //Delete all place images by place id
                    placeDbModels.forEach { placeDbModel ->
                        dataDao.deleteAllPlacesByDistrictId(placeDbModel.id)
                    }
                    //refresh list of places
                    dataDao.deleteAllPlacesByDistrictId(id)
                    dataDao.insertPlaces(placeDbModels)

                    //save place image urls
                    placesDto.forEach { dto ->
                        val imageDbModels =
                            dto.images.map { imageDto -> placeImageMapper.dtoToDbModel(imageDto) }
                        dataDao.insertPlaceImages(imageDbModels)
                    }

                }
            }
        )

    override suspend fun getAllPlaces(): Flow<Resource<List<Place>>> =
        networkBounceResource(
            query = {
                val placesFlow = dataDao.getAllPlaces()
                placesFlow.map { dbModels ->
                    dbModels.map { dbModel ->
                        val imageDbModels = dataDao.getPlaceImagesByPlaceId(dbModel.id)
                        placeMapper.dbModelToEntity(dbModel, imageDbModels)
                    }
                }
            },
            fetch = {
                dataService.getAllPlaces()
            },
            saveFetchResult = { placesDto ->
                db.withTransaction {
                    val placeDbModels = placesDto.map { dto -> placeMapper.dtoToDbModel(dto) }

                    //Delete all place images by place id
                    placeDbModels.forEach { placeDbModel ->
                        dataDao.deleteAllPlacesByDistrictId(placeDbModel.id)
                    }

                    //refresh list of places
                    dataDao.deleteAllPlaces()
                    dataDao.insertPlaces(placeDbModels)

                    //save place image urls
                    placesDto.forEach { dto ->
                        val imageDbModels =
                            dto.images.map { imageDto -> placeImageMapper.dtoToDbModel(imageDto) }
                        dataDao.insertPlaceImages(imageDbModels)
                    }
                }
            }
        )
}