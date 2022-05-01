package com.justnik.mosplace.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.justnik.mosplace.R
import com.justnik.mosplace.data.mappers.JsonMapper
import com.justnik.mosplace.data.network.AuthService
import com.justnik.mosplace.data.network.authmodel.JWT
import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.network.authmodel.UserInfo
import com.justnik.mosplace.domain.AuthRepository
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.helpers.prefs.UserPrefs
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPrefs: UserPrefs,
    private val jsonMapper: JsonMapper
) : AuthRepository {

    override suspend fun createUser(userInfo: UserInfo): Resource<Unit> {
        return try {
            val response = authService.createUser(userInfo)
            if (response.isSuccessful){
                Resource.Success(Unit)
            }
            else{
                val errorBody = JsonParser.parseString(response.errorBody()?.string()).asJsonObject
                val string = jsonMapper.registrationJsonToMessage(errorBody)
                Resource.Error(message = UiText.DynamicText(string))
            }
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(message = UiText.StringResource(R.string.unknown_error))
        }
    }

    override suspend fun loginUser(loginInfo: LoginInfo): Resource<Unit> {
        return try {
            val response = authService.loginUser(loginInfo)
            if (response.isSuccessful) {
                val jsonObject = response.body()
                val gson = Gson()
                val jwt = gson.fromJson(jsonObject, JWT::class.java)
                userPrefs.jwtAccessToken = jwt.access
                userPrefs.jwtRefreshToken = jwt.refresh

                Resource.Success(Unit)
            } else {
                val json = JsonParser.parseString(response.errorBody()?.string()).asJsonObject
                val string = jsonMapper.loginJsonToMessage(json)
                Resource.Error(message = UiText.DynamicText(string))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = UiText.StringResource(R.string.unknown_error))
        }
    }

}