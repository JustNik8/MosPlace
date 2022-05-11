package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.datamodels.ReviewDto
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.entities.profile.Profile
import javax.inject.Inject

class ReviewMapper @Inject constructor(
    private val profileMapper: ProfileMapper
) {
    fun dtoToEntity(dto: ReviewDto): Review {
        return Review(
            id = dto.id,
            profile = profileMapper.dtoToEntity(dto.profile),
            createdDate = dto.createdDate,
            text = dto.text,
            stars = dto.stars,
            placeId = dto.placeId
        )
    }
}