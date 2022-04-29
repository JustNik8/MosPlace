package com.justnik.mosplace.data.repository

import android.util.Log
import com.justnik.mosplace.data.network.AuthService
import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.domain.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun createUser(userFullInfo: UserFullInfo): Resource<JSONObject> {
        return try {
            val json = authService.createUser(userFullInfo)
            Resource.Success(json)
        } catch (e: HttpException) {
            e.printStackTrace()
            val response = e.response()?.errorBody()?.string()
            val responseJSON = JSONObject(response ?: "")
            Resource.Error(message = e.message.toString(), data = responseJSON)
        }
    }

}