package com.justnik.mosplace.data.network.apiservices

import com.justnik.mosplace.data.network.profilemodels.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProfileService {
    @GET("api/v1/profile")
    suspend fun loadProfile(
        @Header(AUTHORIZATION_HEADER_KEY) accessToken: String,
        @Query(QUERY_PARAM_ID) id: Long
    ) : List<ProfileDto>

    companion object{
        private const val QUERY_PARAM_ID = "id"
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
    }
}