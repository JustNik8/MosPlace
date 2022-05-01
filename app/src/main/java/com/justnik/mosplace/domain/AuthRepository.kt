package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.network.authmodel.UserInfo
import com.justnik.mosplace.data.network.authmodel.UserResponse
import com.justnik.mosplace.data.repositories.Resource

interface AuthRepository {
    suspend fun createUser(userInfo: UserInfo): Resource<Unit>
    suspend fun loginUser(loginInfo: LoginInfo) : Resource<Unit>
    suspend fun loadUser(accessToken: String): Resource<UserResponse>
}