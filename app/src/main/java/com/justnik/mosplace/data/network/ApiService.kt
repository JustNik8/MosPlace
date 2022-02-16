package com.justnik.mosplace.data.network

import com.justnik.mosplace.data.network.model.DistrictDto
import retrofit2.http.GET

interface ApiService {
    @GET("places/districts/")
    suspend fun getDistrictList(): List<DistrictDto>
}