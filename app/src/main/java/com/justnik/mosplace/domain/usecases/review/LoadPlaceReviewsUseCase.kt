package com.justnik.mosplace.domain.usecases.review

import com.justnik.mosplace.di.MockReviewRepository
import com.justnik.mosplace.domain.repositories.ReviewRepository
import javax.inject.Inject

class LoadPlaceReviewsUseCase @Inject constructor(
    @MockReviewRepository private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(placeId: Long) = reviewRepository.loadPlaceReviews(placeId)
}