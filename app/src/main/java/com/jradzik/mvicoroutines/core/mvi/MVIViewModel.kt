package com.jradzik.mvicoroutines.core.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MVIViewModel<UserAction : Any, State : Any, SideEffect : Any, Error : Any>(
    initialState: State,
    private val savedStateHandle: SavedStateHandle? = null
) : ViewModel(), MVIState<State> {

    private val mutableState = MutableStateFlow(savedStateHandle?.get(STATE) ?: initialState)
    override val state: StateFlow<State> = mutableState

    // TODO - switch to SharedFlow
    private val sideEffectsChannel = Channel<SideEffect>(Channel.BUFFERED)
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    private val errorChannel = Channel<Error>(Channel.BUFFERED)
    val errors: Flow<Error> = errorChannel.receiveAsFlow()

    fun dispatch(event: UserAction) = handleUserAction(event, mutableState.value)

    protected abstract fun handleUserAction(action: UserAction, currentState: State)

    protected fun updateState(reduce: (State.() -> State)) {
        mutableState.value.let { mutableState.value = reduce(it) }
    }

    fun emitSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            sideEffectsChannel.send(sideEffect)
        }
    }

    fun emitError(error: Error) {
        viewModelScope.launch {
            errorChannel.send(error)
        }
    }

    @CallSuper
    override fun onCleared() {
        savedStateHandle?.set(STATE, mutableState.value)
    }

    companion object {
        private const val STATE = "STATE"
    }
}
