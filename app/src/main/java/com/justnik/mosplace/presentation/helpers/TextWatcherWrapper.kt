package com.justnik.mosplace.presentation.helpers

import android.text.Editable
import android.text.TextWatcher

class TextWatcherWrapper(private val block: () -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Do noting
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        block()
    }

    override fun afterTextChanged(s: Editable?) {
        //Do noting
    }
}