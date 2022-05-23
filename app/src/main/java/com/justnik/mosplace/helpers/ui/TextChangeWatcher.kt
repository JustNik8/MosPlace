package com.justnik.mosplace.helpers.ui

import android.text.Editable
import android.text.TextWatcher

class TextChangeWatcher(
    private val onTextChange: () -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChange()
    }

    override fun afterTextChanged(s: Editable?) {
        //Do nothing
    }
}