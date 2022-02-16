package com.justnik.mosplace.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}