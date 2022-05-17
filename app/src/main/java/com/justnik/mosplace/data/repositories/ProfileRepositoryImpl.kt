package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.mappers.ProfileMapper
import com.justnik.mosplace.data.network.apiservices.ProfileService
import com.justnik.mosplace.data.network.profilemodels.StatusResponse
import com.justnik.mosplace.data.network.profilemodels.VisitedPlaces
import com.justnik.mosplace.data.prefs.ProfilePrefs
import com.justnik.mosplace.domain.repositories.ProfileRepository
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.entities.profile.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val profileMapper: ProfileMapper,
    private val profilePrefs: ProfilePrefs
) : ProfileRepository {
    override suspend fun loadProfile(accessToken: String, id: Long): Resource<Profile> {
        return try {
            val profileDto = profileService.loadProfile("JWT $accessToken", id)
            profilePrefs.profileId = profileDto[0].id
            profilePrefs.visitedPlaceId = profileDto[0].visitedPlaceId

            val profile = profileMapper.dtoToEntity(profileDto[0])
            Resource.Success(profile)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = UiText.DynamicText("Error"))
        }
    }

    override suspend fun addPlaceToProfile(
        accessToken: String,
        profileId: Long,
        placeId: Long
    ): Resource<StatusResponse> {
        return try {
            val status = profileService.addPlaceToProfile("JWT $accessToken", profileId, placeId)
            Resource.Success(status)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = UiText.DynamicText("Error"))
        }
    }

    override suspend fun loadProfileVisitedPlaces(id: Long): Resource<VisitedPlaces> {
        return try {
            val visitedPlaces = profileService.loadProfileVisitedPlaces(id)
            Resource.Success(visitedPlaces[0])
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = UiText.DynamicText("Error"))
        }
    }
}