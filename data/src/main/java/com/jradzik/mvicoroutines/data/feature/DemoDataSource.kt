package com.jradzik.mvicoroutines.data.feature

import com.jradzik.mvicoroutines.data.core.ApiResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DemoDataSource {
    fun reverseText(text: String): Flow<ApiResponse<String>>
}

class DemoDataSourceImpl @Inject constructor() : DemoDataSource {

    override fun reverseText(text: String): Flow<ApiResponse<String>> = flow {
        delay(2000)
        emit(ApiResponse.Ok(text.reversed()))
    }
}