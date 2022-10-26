package com.jradzik.mvicoroutines.extension

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.doOnNextLayout

internal val View.totalPaddingHorizontal
    get() = paddingEnd + paddingStart

fun View.hideKeyboard() {
    clearFocus()

    val inputMethodManager =
        context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    if (isLaidOut) {
        if (requestFocus()) {
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    } else {
        doOnNextLayout {
            if (it.requestFocus()) {
                inputMethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isGone(): Boolean = visibility == View.GONE

fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

fun View.visibleWhen(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.goneWhen(condition: Boolean) {
    visibility = if (condition) View.GONE else View.VISIBLE
}

fun View.invisibleWhen(condition: Boolean) {
    visibility = if (condition) View.INVISIBLE else View.VISIBLE
}
