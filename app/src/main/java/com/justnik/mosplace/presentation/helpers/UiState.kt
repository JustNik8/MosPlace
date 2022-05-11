package com.justnik.mosplace.presentation.helpers

import com.justnik.mosplace.domain.UiText

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: Error? = null
) {
    sealed class Error {
        data class NetworkError(val message: UiText) : Error()
    }
}
