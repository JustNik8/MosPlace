package com.justnik.mosplace.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {

    private var _errorRating = MutableSharedFlow<Boolean>()
    val errorRating = _errorRating.asSharedFlow()

    private var _errorReviewText = MutableStateFlow(false)
    val errorReviewText = _errorReviewText.asStateFlow()

    private var _shouldCloseScreen = MutableSharedFlow<Unit>()
    val shouldCloseScreen = _shouldCloseScreen.asSharedFlow()

    fun validateInputReview(rating: Float, reviewText: String) {
        viewModelScope.launch {
            var isInputValid = true
            if (rating == 0f) {
                _errorRating.emit(true)
                isInputValid = false
            }
            if (reviewText.isEmpty()) {
                _errorReviewText.value = true
                isInputValid = false
            }

            if (isInputValid) {
                _shouldCloseScreen.emit(Unit)
            }
        }
    }

    fun resetErrorReviewText() {
        _errorReviewText.value = false
    }
}