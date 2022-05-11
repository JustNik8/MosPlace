package com.justnik.mosplace.data.network.apiservices

import com.justnik.mosplace.data.network.datamodels.ReviewDto
import retrofit2.http.GET

interface ReviewApiService {

    @GET("")
    fun loadPlaceReviews() : List<ReviewDto>
}