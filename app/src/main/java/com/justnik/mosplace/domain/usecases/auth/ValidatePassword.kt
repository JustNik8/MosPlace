package com.justnik.mosplace.domain.usecases.auth

import javax.inject.Inject

class ValidatePassword @Inject constructor(){
    operator fun invoke(password: String): ValidationResult{
        if (password.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }

        return ValidationResult(successful = true)
    }
}