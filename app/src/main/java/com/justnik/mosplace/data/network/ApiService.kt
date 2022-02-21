package com.justnik.mosplace.data.network

import com.justnik.mosplace.data.network.model.DistrictDto
import com.justnik.mosplace.data.network.model.PlaceDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("places/districts/?format=json")
    suspend fun getDistrictList(): List<DistrictDto>

    @GET("places/places/")
    suspend fun getPlacesByDistrictId(
        @Query(QUERY_PARAM_DISTRICT_ID) districtId: Int,
        @Query(QUERY_PARAM_FORMAT) format: String = "json"
    ): List<PlaceDto>

    companion object {
        private const val QUERY_PARAM_FORMAT = "format"
        private const val QUERY_PARAM_DISTRICT_ID = "district_id"
    }
}