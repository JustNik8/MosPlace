package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.network.AuthService
import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.domain.AuthRepository
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
            val json = JSONObject(e.response()?.errorBody()?.string())
            Resource.Error(message = e.message(), data = json, errorCode = e.code())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

}