package com.justnik.mosplace.domain

import com.justnik.mosplace.data.network.profilemodels.StatusResponse
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.profile.Profile

interface ProfileRepository {
    suspend fun loadProfile(accessToken: String, id: Long): Resource<Profile>
    suspend fun addPlaceToProfile(accessToken: String, profileId: Long, placeId: Long): Resource<StatusResponse>
}