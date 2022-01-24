package com.example.a11task1

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:date")
fun cutDate(view: TextView, date: String) {
    val cutDate = date.substring(0, 10)
    view.text = cutDate
}

@BindingAdapter("app:time")
fun cutTime(view: TextView, time: String) {
    val cutTime = time.substring(11)
    view.text = cutTime
}