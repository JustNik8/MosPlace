package com.justnik.mosplace.data.network

import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/users/")
    suspend fun createUser(@Body userFullInfo: UserFullInfo): JSONObject
}