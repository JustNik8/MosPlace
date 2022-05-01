package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.data.network.authmodel.LoginInfo
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.AuthRepository
import org.json.JSONObject
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(loginInfo: LoginInfo) =
        authRepository.loginUser(loginInfo)
}