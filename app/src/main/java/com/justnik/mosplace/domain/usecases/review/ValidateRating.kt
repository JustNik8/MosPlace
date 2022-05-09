package com.justnik.mosplace.domain.usecases.review

import com.justnik.mosplace.R
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.ValidationResult
import javax.inject.Inject

class ValidateRating @Inject constructor() {
    operator fun invoke(count: Float): ValidationResult {
        if (count == 0F) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.error_rating)
            )
        }
        return ValidationResult(successful = true)
    }
}