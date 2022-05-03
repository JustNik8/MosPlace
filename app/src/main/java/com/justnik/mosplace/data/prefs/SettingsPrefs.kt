package com.justnik.mosplace.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsPrefs @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "com.justnik.mosplace.settings"
        private const val DARK_MODE_CODE_KEY = "dark_mode_code"
    }

    var darkModeCode: Int
        get() = prefs.getInt(DARK_MODE_CODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = prefs.edit().putInt(DARK_MODE_CODE_KEY, value).apply()
}