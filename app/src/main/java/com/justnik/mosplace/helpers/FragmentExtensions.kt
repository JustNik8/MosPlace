package com.justnik.mosplace.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showSupportActionBar() {
    (this.requireActivity() as AppCompatActivity).supportActionBar?.show()
}

fun Fragment.hideSupportActionBar() {
    (this.requireActivity() as AppCompatActivity).supportActionBar?.hide()
}