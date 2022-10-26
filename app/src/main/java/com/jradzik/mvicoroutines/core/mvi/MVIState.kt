package com.jradzik.mvicoroutines.core.mvi

import kotlinx.coroutines.flow.StateFlow

interface MVIState<State : Any> {
    val state: StateFlow<State>
}
