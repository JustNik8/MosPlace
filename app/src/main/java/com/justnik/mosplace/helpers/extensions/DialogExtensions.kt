package com.justnik.mosplace.helpers

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.showSingle(fragmentManager: FragmentManager?, tag: String) {
    if (fragmentManager == null) return
    if (fragmentManager.findFragmentByTag(tag) == null) show(fragmentManager, tag)
}