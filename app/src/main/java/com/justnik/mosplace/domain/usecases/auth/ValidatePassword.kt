package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.R
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(
                    R.string.password_length_error,
                    MIN_PASSWORD_LENGTH
                )
            )
        }

        return ValidationResult(successful = true)
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}