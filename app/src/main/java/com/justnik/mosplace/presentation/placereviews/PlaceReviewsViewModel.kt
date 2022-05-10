package com.justnik.mosplace.presentation.placereviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.usecases.review.LoadPlaceReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class PlaceReviewsViewModel @Inject constructor(
    private val loadPlaceReviewsUseCase: LoadPlaceReviewsUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState<List<Review>>())
    val uiState = _uiState.asStateFlow()

    fun loadPlaceReviews(placeId: Long){
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            when(val resource = loadPlaceReviewsUseCase(placeId)){
                is Resource.Success -> {
                    _uiState.value = UiState(data = resource.data)
                }
                is Resource.Error -> {
                    _uiState.value = UiState(error = UiState.Error.NetworkError(resource.message))
                }
            }
        }

    }

    data class UiState<T>(
        val isLoading: Boolean = false,
        val data: T? = null,
        val error: Error? = null
    ){
        sealed class Error{
            data class NetworkError(val message: UiText): Error()
        }
    }
}