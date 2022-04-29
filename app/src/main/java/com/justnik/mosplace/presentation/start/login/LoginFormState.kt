package com.justnik.mosplace.presentation.start.login

import com.justnik.mosplace.domain.usecases.UiText

data class LoginFormState (
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null
)