package com.justnik.mosplace.domain.usecases.review

import com.justnik.mosplace.data.network.reviewmodels.AddReviewBody
import com.justnik.mosplace.data.repositories.ReviewRepositoryImpl
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepositoryImpl
) {
    suspend operator fun invoke(addReviewBody: AddReviewBody) =
        reviewRepository.addReview(addReviewBody)
}