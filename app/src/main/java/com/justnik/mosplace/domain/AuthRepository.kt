package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.data.repository.Resource
import org.json.JSONObject

interface AuthRepository {
    suspend fun createUser(userFullInfo: UserFullInfo): Resource<JSONObject>
}