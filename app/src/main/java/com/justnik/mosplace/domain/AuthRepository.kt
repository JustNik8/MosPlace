package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.data.repository.Resource

interface AuthRepository {
    suspend fun createUser(userFullInfo: UserFullInfo): Resource<Unit>
}