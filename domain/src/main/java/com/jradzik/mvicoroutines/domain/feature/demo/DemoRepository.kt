package com.jradzik.mvicoroutines.domain.feature.demo

import com.jradzik.mvicoroutines.domain.core.MVICoroutinesResult
import kotlinx.coroutines.flow.Flow

interface DemoRepository {
    fun reverseText(text: String): Flow<MVICoroutinesResult<String>>
}