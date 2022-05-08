package com.justnik.mosplace.data.network.profilemodels

data class StatusResponse (
    val status: String
) {
    companion object {
        const val PLACE_ALREADY_ADDED = "OK"
        const val PLACE_NOT_EXIST = "PlaceDoesNotExist"
        const val PROFILE_NOT_EXIST = "profile"
        const val PLACE_ADDED = "all"
    }

}
