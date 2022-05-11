package com.justnik.mosplace.data.network.apiservices

import com.justnik.mosplace.data.network.datamodels.ReviewDto
import com.justnik.mosplace.data.network.reviewmodels.AddReviewBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewApiService {

    @GET("api/v1/comment_by_article_id")
    suspend fun loadPlaceReviews(
        @Query(QUERY_PARAM_PLACE_ID) placeId: Long
    ) : List<ReviewDto>

    @POST("api/v1/comment_add")
    suspend fun addReview(
        @Body addReviewBody: AddReviewBody
    )

    companion object{
        private const val QUERY_PARAM_PLACE_ID = "place_id"
    }
}