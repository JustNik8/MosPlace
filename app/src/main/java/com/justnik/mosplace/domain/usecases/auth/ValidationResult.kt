package com.justnik.mosplace.domain.usecases.auth

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
