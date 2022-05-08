package com.justnik.mosplace.data.network.apiservices

import com.justnik.mosplace.data.network.profilemodels.AddPlaceToProfileBody
import com.justnik.mosplace.data.network.profilemodels.ProfileDto
import com.justnik.mosplace.data.network.profilemodels.StatusResponse
import retrofit2.http.*

interface ProfileService {
    @GET("api/v1/profile")
    suspend fun loadProfile(
        @Header(AUTHORIZATION_HEADER_KEY) accessToken: String,
        @Query(QUERY_PARAM_ID) id: Long
    ) : List<ProfileDto>

    @POST("api/v1/add_place_to_profile/")
    suspend fun addPlaceToProfile(
        @Header(AUTHORIZATION_HEADER_KEY) accessToken: String,
        @Query(QUERY_PARAM_PROFILE_ID) profileId: Long,
        @Query(QUERY_PARAM_PLACE_ID) placeID: Long
    ) : StatusResponse


    companion object{
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val QUERY_PARAM_ID = "id"
        private const val QUERY_PARAM_PROFILE_ID = "profile_id"
        private const val QUERY_PARAM_PLACE_ID = "place_id"
    }
}