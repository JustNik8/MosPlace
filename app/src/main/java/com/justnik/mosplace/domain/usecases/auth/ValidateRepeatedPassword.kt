package com.justnik.mosplace.domain.usecases.auth

import javax.inject.Inject

class ValidateRepeatedPassword @Inject constructor(){
    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(successful = true)
    }
}