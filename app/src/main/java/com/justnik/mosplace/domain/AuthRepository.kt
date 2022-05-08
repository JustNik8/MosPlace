package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.authmodels.LoginInfo
import com.justnik.mosplace.data.network.authmodels.UserInfo
import com.justnik.mosplace.data.network.authmodels.UserResponse
import com.justnik.mosplace.data.repositories.Resource

interface AuthRepository {
    suspend fun createUser(userInfo: UserInfo): Resource<Unit>
    suspend fun loginUser(loginInfo: LoginInfo) : Resource<Unit>
    suspend fun loadUser(accessToken: String): Resource<UserResponse>
}