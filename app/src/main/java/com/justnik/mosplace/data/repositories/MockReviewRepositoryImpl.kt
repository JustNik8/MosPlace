package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.mappers.ReviewMapper
import com.justnik.mosplace.data.network.apiservices.ReviewApiService
import com.justnik.mosplace.data.network.datamodels.ReviewDto
import com.justnik.mosplace.data.network.profilemodels.ProfileDto
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.repositories.ReviewRepository
import javax.inject.Inject

class MockReviewRepositoryImpl @Inject constructor(
    private val reviewApiService: ReviewApiService,
    private val reviewMapper: ReviewMapper
) : ReviewRepository {

    override suspend fun loadPlaceReviews(placeId: Long): Resource<List<Review>> {
        val reviewsDto = listOf(
            ReviewDto(
                id = 1,
                profile = ProfileDto(1, "Alex", PROFILE1_IMAGE_URL, 1, 1),
                createdDate = "11.05.2022",
                text = "Cool",
                placeId = 1,
                stars = 5
            )
        )
        val reviews = reviewsDto.map { dto-> reviewMapper.dtoToEntity(dto) }
        //val reviews = listOf<Review>()
        return Resource.Success(reviews)
    }


    companion object {
        //Image
        private const val PROFILE1_IMAGE_URL =
            "https://www.fotoget.net/wp-content/uploads/2016/02/man_north-1650x1100.jpg"
    }
}