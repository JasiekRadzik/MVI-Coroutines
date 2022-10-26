package com.jradzik.mvicoroutines.data.feature

import com.jradzik.mvicoroutines.data.core.ApiResponse
import com.jradzik.mvicoroutines.data.core.extensions.toResultErrorType
import com.jradzik.mvicoroutines.domain.core.MVICoroutinesResult
import com.jradzik.mvicoroutines.domain.feature.demo.DemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val demoDataSource: DemoDataSource
) : DemoRepository {
    override fun reverseText(text: String): Flow<MVICoroutinesResult<String>> =
        demoDataSource.reverseText(text).map {
            when(it) {
                is ApiResponse.Ok -> MVICoroutinesResult.Ok(it.data)
                is ApiResponse.Error -> {
                    val errorType = it.exception.toResultErrorType()
                    MVICoroutinesResult.Error(errorType)
                }
            }
        }
}