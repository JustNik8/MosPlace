package com.justnik.mosplace.domain

import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.entities.profile.Profile

interface ProfileRepository {
    suspend fun loadProfile(accessToken: String, id: Long): Resource<Profile>
}