package com.jradzik.mvicoroutines.domain.core

sealed class MVICoroutinesResult<out T> {
    data class Ok<out T>(val value: T) : MVICoroutinesResult<T>()

    data class Error(val type: ResultErrorType = ResultErrorType.UNKNOWN) : MVICoroutinesResult<Nothing>()
}

inline fun <R, T> MVICoroutinesResult<T>.fold(
    onOk: (value: MVICoroutinesResult.Ok<T>) -> R,
    onError: (error: MVICoroutinesResult.Error) -> R
): R {
    return when (this) {
        is MVICoroutinesResult.Ok -> onOk(this)
        is MVICoroutinesResult.Error -> onError(this)
    }
}
