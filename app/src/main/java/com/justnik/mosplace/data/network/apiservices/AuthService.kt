package com.justnik.mosplace.data.network.apiservices

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.justnik.mosplace.data.network.authmodels.LoginInfo
import com.justnik.mosplace.data.network.authmodels.RefreshToken
import com.justnik.mosplace.data.network.authmodels.UserInfo
import com.justnik.mosplace.data.network.authmodels.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/users/")
    suspend fun createUser(@Body userInfo: UserInfo): Response<JsonElement>

    @POST("auth/jwt/create")
    suspend fun loginUser(
        @Body loginInfo: LoginInfo
    ): Response<JsonElement>

    @GET("auth/users/me")
    suspend fun getUser(
        @Header(AUTHORIZATION_HEADER_KEY) accessToken: String
    ): Response<UserResponse>

    @POST("auth/jwt/refresh")
    suspend fun refreshJwtAccessToken(
        @Body refreshToken: RefreshToken
    ) : Response<JsonObject>

    companion object {
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
    }
}