package com.justnik.mosplace.presentation.registration

import com.justnik.mosplace.presentation.helpers.UiText

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