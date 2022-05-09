package com.justnik.mosplace.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.common.ValidateFieldNotBlank
import com.justnik.mosplace.domain.usecases.review.ValidateRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val validateFieldNotBlank: ValidateFieldNotBlank,
    private val validateRating: ValidateRating
) : ViewModel() {

    private var _reviewFormState = MutableStateFlow(ReviewFormState())
    val reviewFormState = _reviewFormState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: ReviewFormEvent) {
        when (event) {
            is ReviewFormEvent.ReviewChanged -> {
                _reviewFormState.value =
                    _reviewFormState.value.copy(review = event.review, reviewError = null)
            }
            is ReviewFormEvent.RatingChanged -> {
                _reviewFormState.value =
                    _reviewFormState.value.copy(ratingCount = event.ratingCount, ratingCountError = null)
            }
            is ReviewFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val reviewResult = validateFieldNotBlank(reviewFormState.value.review)
        val starsCountResult = validateRating(reviewFormState.value.ratingCount)

        val hasError = listOf(reviewResult, starsCountResult).any { !it.successful }
        if (hasError) {
            _reviewFormState.value = reviewFormState.value.copy(
                reviewError = reviewResult.errorMessage,
                ratingCountError = starsCountResult.errorMessage
            )
            return
        }

        saveReview()
    }

    private fun saveReview() {
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success(UiText.DynamicText("Success")))
        }
    }

    sealed class ValidationEvent {
        data class Success(val message: UiText) : ValidationEvent()
        data class Error(val message: UiText) : ValidationEvent()
    }
}