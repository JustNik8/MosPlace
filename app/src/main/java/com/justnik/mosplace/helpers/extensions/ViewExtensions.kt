package com.justnik.mosplace.helpers

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.justnik.mosplace.helpers.ui.UiText

fun View.showSnackbar(message: UiText, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message.asString(this.context), length).show()
}