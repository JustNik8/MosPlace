package com.justnik.mosplace.domain.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
