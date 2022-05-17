package com.justnik.mosplace.presentation.placereviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.usecases.review.LoadPlaceReviewsUseCase
import com.justnik.mosplace.presentation.helpers.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceReviewsViewModel @Inject constructor(
    private val loadPlaceReviewsUseCase: LoadPlaceReviewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<List<Review>>())
    val uiState = _uiState.asStateFlow()

    fun loadPlaceReviews(placeId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when (val resource = loadPlaceReviewsUseCase(placeId)) {
                is Resource.Success -> {
                    _uiState.value = UiState(data = resource.data)
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError(resource.message))
                }
            }
        }
    }

    fun getLastReviews(): List<Review>? {
        val reviews = uiState.value.data ?: return null
        val size = reviews.size
        val maxSizeToSlice = if (size < 3) size else 3
        return reviews.slice(0 until maxSizeToSlice)
    }
}