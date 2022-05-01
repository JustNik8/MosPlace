package com.justnik.mosplace.domain.usecases.profile

import com.justnik.mosplace.domain.ProfileRepository
import javax.inject.Inject

class LoadProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(accessToken: String, id: Long) =
        profileRepository.loadProfile(accessToken, id)
}