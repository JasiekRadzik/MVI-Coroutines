package com.jradzik.mvicoroutines.domain.feature.demo

import com.jradzik.mvicoroutines.domain.core.MVICoroutinesResult
import com.jradzik.mvicoroutines.domain.core.UseCase
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class ReverseTextUseCase @Inject constructor(
    private val demoRepository: DemoRepository
) : UseCase<ReverseTextRequestModel, MVICoroutinesResult<String>> {

    override suspend fun invoke(param: ReverseTextRequestModel): MVICoroutinesResult<String> =
        demoRepository.reverseText(param.text)
            .single()
}