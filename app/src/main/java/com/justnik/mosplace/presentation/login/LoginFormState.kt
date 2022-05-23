package com.justnik.mosplace.presentation.login

import com.justnik.mosplace.helpers.ui.UiText

data class LoginFormState (
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null
)