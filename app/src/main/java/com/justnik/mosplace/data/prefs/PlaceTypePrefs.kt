package com.justnik.mosplace.data.prefs

import android.content.Context
import com.justnik.mosplace.data.network.PlaceTypes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlaceTypePrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "com.justnik.mosplace.type"
    }

    fun setTypeSelection(prefsType: PrefsType) {
        prefs.edit().putBoolean(prefsType.typeName, prefsType.selected).apply()
    }

    val selectedPrefsTypes: List<PrefsType>
        get() {
            val availableTypes = getAvailableTypes()
            val prefsTypes = mutableListOf<PrefsType>()

            for (i in availableTypes.indices) {
                val type = availableTypes[i]
                val selected = prefs.getBoolean(type, true)
                prefsTypes.add(PrefsType(type, selected))
            }
            return prefsTypes
        }

    private fun getAvailableTypes(): Array<String> {
        return arrayOf(
            PlaceTypes.UNIQUE_PLACE,
            PlaceTypes.RESTAURANT,
            PlaceTypes.PARK
        )
    }

}

data class PrefsType(
    val typeName: String,
    var selected: Boolean
)