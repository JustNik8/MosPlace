package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.presentation.helpers.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
