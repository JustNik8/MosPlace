package com.justnik.mosplace.domain.usecases.auth

import com.justnik.mosplace.R
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidateRepeatedPassword @Inject constructor(){
    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.passwords_dont_match_error)
            )
        }
        return ValidationResult(successful = true)
    }
}