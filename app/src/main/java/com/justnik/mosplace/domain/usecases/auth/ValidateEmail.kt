package com.justnik.mosplace.domain.usecases.auth

import android.util.Patterns
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "Invalid email"
            )
        }
        return ValidationResult(successful = true)
    }
}