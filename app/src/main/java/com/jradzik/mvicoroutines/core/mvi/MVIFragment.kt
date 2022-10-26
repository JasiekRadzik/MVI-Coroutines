package com.jradzik.mvicoroutines.core.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jradzik.mvicoroutines.extension.repeatOnStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class MVIFragment<UserAction : Any, State : Any, SideEffect : Any, Error : Any> : Fragment() {
    protected abstract val viewModel: MVIViewModel<UserAction, State, SideEffect, Error>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.onEach {
                render(it)
            }.launchIn(this)
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.onEach {
                handleSideEffect(it)
            }.launchIn(this)
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errors.onEach {
                handleError(it)
            }.launchIn(this)
        }
    }
    protected abstract fun render(viewState: State)
    protected abstract fun handleSideEffect(sideEffect: SideEffect)
    protected abstract fun handleError(error: Error)
}
