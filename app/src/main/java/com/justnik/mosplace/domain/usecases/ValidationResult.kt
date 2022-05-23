package com.justnik.mosplace.domain.usecases

import com.justnik.mosplace.helpers.ui.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
