package com.justnik.mosplace.data.network

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.data.network.model.PlaceDto
import com.justnik.mosplace.domain.entities.Place
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {
    @GET("districts")
    suspend fun getDistrictList(
        @Query(QUERY_PARAM_FORMAT) format: String = JSON
    ): List<DistrictDto>

    @GET("places")
    suspend fun getPlacesByDistrictId(
        @Query(QUERY_PARAM_DISTRICT_ID) districtId: Int,
        @Query(QUERY_PARAM_FORMAT) format: String = JSON
    ): List<PlaceDto>

    @GET("places")
    suspend fun getAllPlaces(): List<PlaceDto>

    companion object {
        private const val QUERY_PARAM_FORMAT = "format"
        private const val QUERY_PARAM_DISTRICT_ID = "district_id"
        private const val JSON = "json"
    }
}