package com.jradzik.mvicoroutines.feature.demo

import androidx.lifecycle.viewModelScope
import com.jradzik.mvicoroutines.domain.core.MVICoroutinesResult
import com.jradzik.mvicoroutines.domain.feature.demo.ReverseTextRequestModel
import com.jradzik.mvicoroutines.domain.feature.demo.ReverseTextUseCase
import com.jradzik.mvicoroutines.R
import com.jradzik.mvicoroutines.core.mvi.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val reverseTextUseCase: ReverseTextUseCase
) : MVIViewModel<DemoUserAction, DemoState, DemoSideEffect, DemoError>(DemoState.Default) {

    override fun handleUserAction(action: DemoUserAction, currentState: DemoState) {
        when (action) {
            DemoUserAction.ReverseTextClicked -> reverseText(currentState)
            is DemoUserAction.EnableReverseTextClicked -> updateState { copy(isReverseTextEnabled = !currentState.isReverseTextEnabled) }
            DemoUserAction.ResetClicked -> {
                updateState { DemoState.Default }
                emitSideEffect(DemoSideEffect.ShowInfo(R.string.demo_reset_info))
            }
        }
    }

    private fun reverseText(currentState: DemoState) = viewModelScope.launch {
        updateState { copy(isLoading = true) }
        reverseTextUseCase(ReverseTextRequestModel(currentState.text)).let { response ->
            when (response) {
                is MVICoroutinesResult.Ok -> {
                    updateState { copy(isLoading = false, text = response.value) }
                    emitSideEffect(DemoSideEffect.ShowSuccess(R.string.demo_reverse_text_success))
                }
                is MVICoroutinesResult.Error -> {
                    updateState { copy(isLoading = false) }
                    emitError(DemoError(R.string.demo_reverse_text_error))
                }
            }
        }
    }
}
