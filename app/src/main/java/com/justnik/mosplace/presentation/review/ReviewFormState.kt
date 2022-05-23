package com.justnik.mosplace.presentation.review

import com.justnik.mosplace.helpers.ui.UiText

data class ReviewFormState(
    val review: String = "",
    val reviewError: UiText? = null,
    val ratingCount: Float = 0F,
    val ratingCountError: UiText? = null
)
