package com.justnik.mosplace.data.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfilePrefs @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var profileId: Long
        get() = prefs.getLong(PROFILE_ID_KEY, -1)
        set(value) = prefs.edit().putLong(PROFILE_ID_KEY, value).apply()

    var visitedPlaceId: Long
        get() = prefs.getLong(VISITED_PLACE_ID_KEY, -1)
        set(value) = prefs.edit().putLong(VISITED_PLACE_ID_KEY, value).apply()

    companion object {
        private const val PREFS_NAME = "com.justnik.mosplace.profile"
        private const val PROFILE_ID_KEY = "profile_id"
        private const val VISITED_PLACE_ID_KEY = "visited_place_id"
    }
}