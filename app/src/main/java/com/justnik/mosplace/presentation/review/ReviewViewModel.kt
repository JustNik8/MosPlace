package com.justnik.mosplace.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(): ViewModel() {

    private var _errorRating = MutableLiveData<Boolean>()
    val errorRating: LiveData<Boolean>
        get() = _errorRating

    private var _errorReviewText = MutableLiveData<Boolean>()
    val errorReviewText: LiveData<Boolean>
        get() = _errorReviewText

    private var _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun validateInputReview(rating: Float, reviewText: String) {
        var isInputValid = true
        if (rating == 0f) {
            _errorRating.value = true
            isInputValid = false
        }
        if (reviewText.isEmpty()) {
            _errorReviewText.value = true
            isInputValid = false
        }

        if (isInputValid) {
            _shouldCloseScreen.value = Unit
        }
    }

    fun resetErrorReviewText() {
        _errorReviewText.value = false
    }
}