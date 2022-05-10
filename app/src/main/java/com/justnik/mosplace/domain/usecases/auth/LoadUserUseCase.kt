package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.domain.repositories.AuthRepository
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String) = authRepository.loadUser(accessToken)
}