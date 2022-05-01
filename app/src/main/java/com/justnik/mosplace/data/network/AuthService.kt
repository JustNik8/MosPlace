package com.justnik.mosplace.data.network

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.network.authmodel.UserInfo
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/users/")
    suspend fun createUser(@Body userInfo: UserInfo): Response<JsonElement>

    @POST("auth/jwt/create")
    suspend fun loginUser(
        @Body loginInfo: LoginInfo
    ): Response<JsonElement>
}