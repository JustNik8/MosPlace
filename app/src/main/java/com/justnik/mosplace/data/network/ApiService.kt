package com.justnik.mosplace.data.network

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.data.network.model.PlaceDto
import com.justnik.mosplace.domain.entities.Place
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {
    @GET("api/v1/districts")
    suspend fun getDistrictList(
        @Query(QUERY_PARAM_FORMAT) format: String = JSON
    ): List<DistrictDto>

    @GET("api/v1/places")
    suspend fun getPlacesByDistrictId(
        @Query(QUERY_PARAM_DISTRICT_ID) districtId: Int,
        @Query(QUERY_PARAM_FORMAT) format: String = JSON
    ): List<PlaceDto>

    @GET("api/v1/places")
    suspend fun getAllPlaces(
        @Query(QUERY_PARAM_FORMAT) format: String = JSON
    ): List<PlaceDto>

    companion object {
        private const val QUERY_PARAM_FORMAT = "format"
        private const val QUERY_PARAM_DISTRICT_ID = "district_id"
        private const val JSON = "json"
    }
}