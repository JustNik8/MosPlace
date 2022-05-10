package com.justnik.mosplace.data.repositories

import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.domain.entities.Review
import com.justnik.mosplace.domain.repositories.ReviewRepository
import javax.inject.Inject

class MockReviewRepositoryImpl @Inject constructor() : ReviewRepository {

    override suspend fun loadPlaceReviews(placeId: Long): Resource<List<Review>> {
        return Resource.Success(createReviews())
    }

    private fun createReviews() = listOf(
        Review(1, PROFILE1_IMAGE_URL, PROFILE_NAME1, 4, DATE1, TEXT1),
        Review(2, PROFILE2_IMAGE_URL, PROFILE_NAME2, 5, DATE2, TEXT2),
        Review(3, PROFILE3_IMAGE_URL, PROFILE_NAME3, 3, DATE3, TEXT3),
        Review(4, PROFILE4_IMAGE_URL, PROFILE_NAME4, 1, DATE4, TEXT4),
        Review(5, PROFILE5_IMAGE_URL, PROFILE_NAME5, 2, DATE5, TEXT5),
        Review(6, PROFILE6_IMAGE_URL, PROFILE_NAME6, 5, DATE6, TEXT6),
        Review(7, PROFILE7_IMAGE_URL, PROFILE_NAME7, 5, DATE7, TEXT7),
        Review(8, PROFILE8_IMAGE_URL, PROFILE_NAME8, 4, DATE8, TEXT8)
    )

    companion object {
        //Images
        private const val PROFILE1_IMAGE_URL =
            "https://www.fotoget.net/wp-content/uploads/2016/02/man_north-1650x1100.jpg"
        private const val PROFILE2_IMAGE_URL =
            "https://i.pinimg.com/564x/59/73/ff/5973ff63055dc63a35e317d4ac3b08e0.jpg"
        private const val PROFILE3_IMAGE_URL =
            "https://i.pinimg.com/564x/a0/5f/6f/a05f6f20728289fd9f5ec22ce3592271.jpg"
        private const val PROFILE4_IMAGE_URL =
            "https://i.pinimg.com/564x/16/d5/fb/16d5fb7f9596ea39c41d6a21bc0c3f36.jpg"
        private const val PROFILE5_IMAGE_URL =
            "https://i.pinimg.com/564x/37/2c/da/372cda2fca4046919d7f0a2eb70a09db.jpg"
        private const val PROFILE6_IMAGE_URL =
            "https://i.pinimg.com/564x/aa/97/ee/aa97ee6003d905fda465c321b73fde2c.jpg"
        private const val PROFILE7_IMAGE_URL =
            "https://i.pinimg.com/564x/2d/ed/1f/2ded1f0e703fe12f331e08a6314089f3.jpg"
        private const val PROFILE8_IMAGE_URL =
            "https://i.pinimg.com/564x/0d/c9/c4/0dc9c404e51e9dc63e12dc7e6adc8e2f.jpg"

        //Profile names
        private const val PROFILE_NAME1 = "Alex"
        private const val PROFILE_NAME2 = "Vasilisa Andreevna"
        private const val PROFILE_NAME3 = "Egorova Sofia"
        private const val PROFILE_NAME4 = "Markov Egor"
        private const val PROFILE_NAME5 = "Ivanov Artyom"
        private const val PROFILE_NAME6 = "Sotnikov Andrey"
        private const val PROFILE_NAME7 = "Zubova Angelina"
        private const val PROFILE_NAME8 = "Serova Nikol'"


        //Dates
        private const val DATE1 = "10.05.2022"
        private const val DATE2 = "04.07.2022"
        private const val DATE3 = "02.06.2022"
        private const val DATE4 = "16.12.2021"
        private const val DATE5 = "28.03.2022"
        private const val DATE6 = "19.01.2022"
        private const val DATE7 = "18.03.2022"
        private const val DATE8 = "07.02.2022"

        //Review text
        private const val TEXT1 = "Great"
        private const val TEXT2 = "I like this place!"
        private const val TEXT3 = "Nothing special"
        private const val TEXT4 = "That's such an awful place. I am going to never visit this"
        private const val TEXT5 = "Bad"
        private const val TEXT6 = "The best place I have visited"
        private const val TEXT7 = "Amazing!"
        private const val TEXT8 = "Good, but has some disadvantages"
    }
}