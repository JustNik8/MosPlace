package com.justnik.mosplace.domain.usecases.auth

import javax.inject.Inject

class ValidateUsername @Inject constructor(){
    operator fun invoke(username: String): ValidationResult{
        if (username.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Username can't be blank"
            )
        }
        return ValidationResult(successful = true)
    }
}