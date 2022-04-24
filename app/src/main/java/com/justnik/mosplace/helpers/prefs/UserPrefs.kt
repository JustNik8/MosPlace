package com.justnik.mosplace.helpers.prefs

import android.content.Context
import android.content.SharedPreferences

class UserPrefs (private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "com.justnik.mosplace.user"
        private const val ON_BOARDING_FINISHED_KEY = "on_boarding_finished"
    }

    var isOnBoardingFinished: Boolean
        get() = prefs.getBoolean(ON_BOARDING_FINISHED_KEY, false)
        set(value) = prefs.edit().putBoolean(ON_BOARDING_FINISHED_KEY, value).apply()
}