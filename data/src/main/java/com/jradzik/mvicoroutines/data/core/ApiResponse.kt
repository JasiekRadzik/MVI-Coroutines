package com.jradzik.mvicoroutines.data.core

import com.jradzik.mvicoroutines.data.core.exceptions.MVICoroutinesException

sealed class ApiResponse<out T> {
    data class Ok<out T>(val data: T) : ApiResponse<T>()

    data class Error(val exception: MVICoroutinesException) : ApiResponse<Nothing>()
}
