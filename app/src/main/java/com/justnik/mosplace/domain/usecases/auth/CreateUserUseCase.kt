package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.data.network.authmodels.UserInfo
import com.justnik.mosplace.domain.repositories.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userInfo: UserInfo) =
        authRepository.createUser(userInfo)

}