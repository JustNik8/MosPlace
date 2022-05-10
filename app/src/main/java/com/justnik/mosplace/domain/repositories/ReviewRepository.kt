package com.justnik.mosplace.domain.repositories

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.Review

interface ReviewRepository {
    suspend fun loadPlaceReviews(placeId: Long): Resource<List<Review>>
}