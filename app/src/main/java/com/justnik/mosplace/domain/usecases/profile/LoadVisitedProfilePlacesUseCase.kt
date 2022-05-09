package com.justnik.mosplace.domain.usecases.profile

import com.justnik.mosplace.data.network.apiservices.ProfileService
import com.justnik.mosplace.domain.ProfileRepository
import javax.inject.Inject

class LoadVisitedProfilePlacesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(id: Long) = profileRepository.loadProfileVisitedPlaces(id)
}