package com.justnik.mosplace.presentation.helpers

data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)