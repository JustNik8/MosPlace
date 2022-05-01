package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.data.mappers.ProfileMapper
import com.justnik.mosplace.data.network.apiservices.ProfileService
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.domain.ProfileRepository
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.profile.Profile
import java.lang.Exception
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val profileMapper: ProfileMapper,
): ProfileRepository {
    override suspend fun loadProfile(accessToken: String, id: Long): Resource<Profile> {
        return try {
            val profileDto = profileService.loadProfile("JWT $accessToken", id)
            val profile = profileMapper.dtoToEntity(profileDto[0])
            Resource.Success(profile)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(message = UiText.DynamicText("Error"))
        }
    }
}