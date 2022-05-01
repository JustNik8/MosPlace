package com.justnik.mosplace.presentation.start.registration

import com.justnik.mosplace.domain.UiText

data class RegistrationFormState (
    val email: String = "",
    val emailError: UiText? = null,
    val username: String = "",
    val usernameError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: UiText? = null
)