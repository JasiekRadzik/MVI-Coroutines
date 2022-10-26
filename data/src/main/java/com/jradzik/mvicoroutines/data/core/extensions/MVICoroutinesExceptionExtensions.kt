package com.jradzik.mvicoroutines.data.core.extensions

import com.jradzik.mvicoroutines.data.core.exceptions.MVICoroutinesException
import com.jradzik.mvicoroutines.data.core.exceptions.MVICoroutinesNetworkException
import com.jradzik.mvicoroutines.data.core.exceptions.MVICoroutinesUnknownException
import com.jradzik.mvicoroutines.domain.core.ResultErrorType

@Suppress("LongParameterList")
fun MVICoroutinesException.toResultErrorType(
    missingDataErrorType: ResultErrorType = ResultErrorType.MISSING_DATA,
    networkErrorType: ResultErrorType = ResultErrorType.NETWORK,
): ResultErrorType = when (this) {
    is MVICoroutinesUnknownException -> ResultErrorType.UNKNOWN
    is MVICoroutinesNetworkException -> networkErrorType
    else -> ResultErrorType.UNKNOWN
}
