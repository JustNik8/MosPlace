package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.R
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidateUsername @Inject constructor(){
    operator fun invoke(username: String): ValidationResult {
        if (username.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.username_blank_error)
            )
        }
        return ValidationResult(successful = true)
    }
}