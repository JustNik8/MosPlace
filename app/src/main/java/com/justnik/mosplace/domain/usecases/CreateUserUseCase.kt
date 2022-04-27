package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userFullInfo: UserFullInfo): Resource<Unit> =
        authRepository.createUser(userFullInfo)

}