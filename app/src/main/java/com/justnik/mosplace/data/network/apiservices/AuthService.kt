package com.justnik.mosplace.data.network.apiservices

import com.google.gson.JsonElement
import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.network.authmodel.UserInfo
import com.justnik.mosplace.data.network.authmodel.UserResponse
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
    suspend fun getUser(@Header(AUTHORIZATION_HEADER_KEY) accessToken: String): Response<UserResponse>

    companion object {
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
    }
}