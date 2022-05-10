package com.justnik.mosplace.helpers

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showSupportActionBar() {
    (this.requireActivity() as AppCompatActivity).supportActionBar?.show()
}

fun Fragment.hideSupportActionBar() {
    (this.requireActivity() as AppCompatActivity).supportActionBar?.hide()
}

fun Fragment.setTitle(@StringRes stringRes: Int) {
    (this.requireActivity() as AppCompatActivity).setTitle(stringRes)
}

fun Fragment.setTitle(title: String){
    (this.requireActivity() as AppCompatActivity).title = title
}