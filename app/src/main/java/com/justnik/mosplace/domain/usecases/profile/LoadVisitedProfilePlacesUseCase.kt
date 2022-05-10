package com.justnik.mosplace.domain.usecases.profile

import com.justnik.mosplace.domain.repositories.ProfileRepository
import javax.inject.Inject

class LoadVisitedProfilePlacesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(id: Long) = profileRepository.loadProfileVisitedPlaces(id)
}