package com.justnik.mosplace.data.mappers

import com.justnik.mosplace.data.network.profilemodels.ProfileDto
import com.justnik.mosplace.domain.entities.profile.Profile
import javax.inject.Inject

class ProfileMapper @Inject constructor() {
    fun dtoToEntity(dto: ProfileDto): Profile {
        return Profile(
            id = dto.id,
            name = dto.name,
            imageUrl = dto.imageUrl
        )
    }
}