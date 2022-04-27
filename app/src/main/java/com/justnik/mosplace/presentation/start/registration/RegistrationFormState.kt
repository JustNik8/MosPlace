package com.justnik.mosplace.presentation.start.registration

data class RegistrationFormState (
    val email: String = "",
    val emailError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null
)