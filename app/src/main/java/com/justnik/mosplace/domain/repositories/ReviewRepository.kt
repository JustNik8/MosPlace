package com.justnik.mosplace.domain.repositories

import com.justnik.mosplace.helpers.Resource
import com.justnik.mosplace.data.network.reviewmodels.AddReviewBody
import com.justnik.mosplace.domain.entities.Review

interface ReviewRepository {
    suspend fun loadPlaceReviews(placeId: Long): Resource<List<Review>>
    suspend fun addReview(addReviewBody: AddReviewBody): Resource<Unit>
}