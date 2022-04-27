package com.justnik.mosplace.data.repository

import com.justnik.mosplace.data.network.AuthService
import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.domain.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun createUser(userFullInfo: UserFullInfo): Resource<Unit> {
        return try {
            authService.createUser(userFullInfo)
            Resource.Success(Unit)
        } catch (e: HttpException){
            Resource.Error(message = e.message(), errorCode = e.code())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

}