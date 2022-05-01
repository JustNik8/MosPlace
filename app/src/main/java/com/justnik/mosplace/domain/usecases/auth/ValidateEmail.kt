package com.justnik.mosplace.domain.usecases.auth

import android.util.Patterns
import com.justnik.mosplace.R
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_blank_error)
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_invalid_error)
            )
        }
        return ValidationResult(successful = true)
    }
}