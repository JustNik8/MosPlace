package com.justnik.mosplace.domain.usecases.profile

import com.justnik.mosplace.domain.ProfileRepository
import javax.inject.Inject

class AddPlaceToProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
){
    suspend operator fun invoke(
        accessToken: String,
        profileId: Long,
        placeId: Long
    ) = profileRepository.addPlaceToProfile(accessToken, profileId, placeId)
}