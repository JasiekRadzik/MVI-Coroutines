package com.jradzik.mvicoroutines.data.core.exceptions

sealed class MVICoroutinesException(
    override val message: String?,
    override val cause: Throwable?
) : Exception(message, cause)

data class MVICoroutinesUnknownException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : MVICoroutinesException(message, cause)

data class MVICoroutinesNetworkException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : MVICoroutinesException(message, cause)
