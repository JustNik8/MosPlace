package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.network.authmodel.UserInfo
import com.justnik.mosplace.data.repository.Resource
import org.json.JSONObject

interface AuthRepository {
    suspend fun createUser(userInfo: UserInfo): Resource<Unit>
    suspend fun loginUser(loginInfo: LoginInfo) : Resource<Unit>
}