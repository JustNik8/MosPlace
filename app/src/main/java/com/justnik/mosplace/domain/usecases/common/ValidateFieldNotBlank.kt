package com.justnik.mosplace.domain.usecases.common

import com.justnik.mosplace.R
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidateFieldNotBlank @Inject constructor() {
    operator fun invoke(field: String): ValidationResult {
        if (field.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.field_blank_error)
            )
        }
        return ValidationResult(successful = true)
    }
}