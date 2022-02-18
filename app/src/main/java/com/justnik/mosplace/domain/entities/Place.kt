package com.justnik.mosplace.domain.entities

data class Place (
    val id: Int,
    val images: List<PlaceImage>,
    val title: String,
    val description: String,
    val type: String,
    val district: Int
)