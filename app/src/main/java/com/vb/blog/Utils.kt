package com.vb.blog

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun View.hideKeyboard() {
    this.context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(this.windowToken, 0)
}
