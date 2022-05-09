package com.justnik.mosplace.presentation.review

sealed class ReviewFormEvent {
    data class ReviewChanged(val review: String) : ReviewFormEvent()
    data class RatingChanged(val ratingCount: Float) : ReviewFormEvent()
    object Submit : ReviewFormEvent()
}
