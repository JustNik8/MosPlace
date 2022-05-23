package com.justnik.mosplace.helpers.ui

data class UiState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)