package com.justnik.mosplace.presentation.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.network.reviewmodels.AddReviewBody
import com.justnik.mosplace.data.prefs.ProfilePrefs
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.usecases.common.ValidateFieldNotBlank
import com.justnik.mosplace.domain.usecases.review.AddReviewUseCase
import com.justnik.mosplace.domain.usecases.review.ValidateRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val validateFieldNotBlank: ValidateFieldNotBlank,
    private val validateRating: ValidateRating,
    private val addReviewUseCase: AddReviewUseCase,
    private val profilePrefs: ProfilePrefs
) : ViewModel() {

    private var _reviewFormState = MutableStateFlow(ReviewFormState())
    val reviewFormState = _reviewFormState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private var place: Place? = null

    fun setCurrentPlace(place: Place) {
        this.place = place
    }

    fun onEvent(event: ReviewFormEvent) {
        when (event) {
            is ReviewFormEvent.ReviewChanged -> {
                _reviewFormState.value =
                    _reviewFormState.value.copy(review = event.review, reviewError = null)
            }
            is ReviewFormEvent.RatingChanged -> {
                _reviewFormState.value =
                    _reviewFormState.value.copy(
                        ratingCount = event.ratingCount,
                        ratingCountError = null
                    )
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

        addReview()
    }

    private fun addReview() {
        viewModelScope.launch {
            val place = this@ReviewViewModel.place
                ?: throw RuntimeException("You should set current place via method setCurrentPlace(place: Place)")
            val createdDate = getCurrentFormattedDate()
            val reviewText = reviewFormState.value.review
            val rating = reviewFormState.value.ratingCount.toInt()

            val addReviewBody = AddReviewBody(
                createdDate = createdDate,
                text = reviewText,
                stars = rating,
                placeId = place.id.toLong(),
                profileId = profilePrefs.profileId
            )
            when (val resource = addReviewUseCase(addReviewBody)){
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success(UiText.DynamicText("Success")))
                }
                is Resource.Error -> {
                    validationEventChannel.send(ValidationEvent.Success(UiText.DynamicText("Error")))
                }
            }
        }
    }

    private fun getCurrentFormattedDate(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val currentDate = sdf.format(Date())
        Log.d("RRR", currentDate)
        return currentDate
    }

    sealed class ValidationEvent {
        data class Success(val message: UiText) : ValidationEvent()
        data class Error(val message: UiText) : ValidationEvent()
    }

    companion object {
//        private const val DATE_FORMAT = "dd.MM.yyyy HH:mm:ss"
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}