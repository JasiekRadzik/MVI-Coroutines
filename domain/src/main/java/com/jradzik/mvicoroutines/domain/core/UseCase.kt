package com.jradzik.mvicoroutines.domain.core

import kotlinx.coroutines.flow.Flow

interface UseCase<Param, Result> {
    suspend operator fun invoke(param: Param): Result
}

interface FlowUseCase<Param, Result> {
    suspend operator fun invoke(param: Param): Flow<Result>
}

object NoParam

object NoResult
