package com.justnik.mosplace.data.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPrefs @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "com.justnik.mosplace.user"
        private const val ON_BOARDING_FINISHED_KEY = "on_boarding_finished"
        private const val JWT_ACCESS_TOKEN_KEY = "jwt_access_token"
        private const val JWT_REFRESH_TOKEN_KEY = "jwt_refresh_token"
    }

    var isOnBoardingFinished: Boolean
        get() = prefs.getBoolean(ON_BOARDING_FINISHED_KEY, false)
        set(value) = prefs.edit().putBoolean(ON_BOARDING_FINISHED_KEY, value).apply()

    var jwtAccessToken: String?
        get() = prefs.getString(JWT_ACCESS_TOKEN_KEY, null)
        set(value) = prefs.edit().putString(JWT_ACCESS_TOKEN_KEY, value).apply()

    var jwtRefreshToken: String?
        get() = prefs.getString(JWT_REFRESH_TOKEN_KEY, null)
        set(value) = prefs.edit().putString(JWT_REFRESH_TOKEN_KEY, value).apply()
}