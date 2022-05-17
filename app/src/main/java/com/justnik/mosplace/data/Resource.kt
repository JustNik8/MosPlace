package com.justnik.mosplace.data

import com.justnik.mosplace.presentation.helpers.UiText

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: UiText, val data: T? = null) : Resource<T>()
}
