package com.justnik.mosplace.extensions

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(bottomNavigationView: BottomNavigationView){
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavigationView(bottomNavigationView: BottomNavigationView){
    bottomNavigationView.visibility = View.VISIBLE
}
