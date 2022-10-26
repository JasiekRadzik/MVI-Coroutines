package com.jradzik.mvicoroutines.feature.demo

import androidx.annotation.StringRes

private const val DEFAULT_TEXT = "Let's see how the MVI with Kotlin Coroutines works"

data class DemoState(
    val isLoading: Boolean = false,
    val text: String = DEFAULT_TEXT,
    val isReverseTextEnabled: Boolean = true
) {
    companion object {
        val Default: DemoState
            get() = DemoState()
    }
}

sealed class DemoUserAction {
    object ResetClicked : DemoUserAction()
    object ReverseTextClicked : DemoUserAction()
    object EnableReverseTextClicked : DemoUserAction()
}

sealed class DemoSideEffect {
    data class ShowInfo(@StringRes val textResId: Int) : DemoSideEffect()
    data class ShowSuccess(@StringRes val textResId: Int) : DemoSideEffect()
}

data class DemoError(@StringRes val errorResId: Int)
