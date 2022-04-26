package com.justnik.mosplace.domain.entities

import androidx.annotation.DrawableRes

data class OnBoardingItem (
    val title: String,
    val desc: String,
    @DrawableRes val drawableId: Int
)