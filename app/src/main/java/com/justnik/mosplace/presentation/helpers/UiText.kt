package com.justnik.mosplace.presentation.helpers

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicText(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int, vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicText -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}
