package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.data.prefs.UserPrefs
import javax.inject.Inject

class IsUserAuthorizedUseCase @Inject constructor(
    private val userPrefs: UserPrefs
) {
    operator fun invoke() = userPrefs.jwtAccessToken != null
}