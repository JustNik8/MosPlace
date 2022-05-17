package com.justnik.mosplace.presentation.helpers

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: Error? = null
) {
    sealed class Error {
        data class NetworkError(val message: UiText) : Error()
    }
}
