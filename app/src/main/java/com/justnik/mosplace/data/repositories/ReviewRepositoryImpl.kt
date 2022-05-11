package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.mappers.ReviewMapper
import com.justnik.mosplace.data.network.apiservices.ReviewApiService
import com.justnik.mosplace.data.network.reviewmodels.AddReviewBody
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.repositories.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApiService: ReviewApiService,
    private val reviewMapper: ReviewMapper
): ReviewRepository {
    override suspend fun loadPlaceReviews(placeId: Long): Resource<List<Review>> {
        return try {
            val placeReviewsDto = reviewApiService.loadPlaceReviews(placeId)
            val placeReviews = placeReviewsDto.map { dto -> reviewMapper.dtoToEntity(dto) }

            Resource.Success(placeReviews)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(UiText.DynamicText("Error"))
        }

    }

    override suspend fun addReview(addReviewBody: AddReviewBody): Resource<Unit> {
        return try{
            reviewApiService.addReview(addReviewBody)
            Resource.Success(Unit)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Error(UiText.DynamicText("Error"))
        }
    }
}