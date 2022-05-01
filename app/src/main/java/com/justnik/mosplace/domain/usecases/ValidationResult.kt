package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.domain.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
