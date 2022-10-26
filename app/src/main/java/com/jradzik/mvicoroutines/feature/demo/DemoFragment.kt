package com.jradzik.mvicoroutines.feature.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jradzik.mvicoroutines.R
import com.jradzik.mvicoroutines.databinding.FragmentDemoBinding
import com.jradzik.mvicoroutines.core.mvi.MVIFragment
import com.jradzik.mvicoroutines.core.viewbinding.LifecycleViewBindingHolder
import com.jradzik.mvicoroutines.core.viewbinding.ViewBindingHolder
import com.jradzik.mvicoroutines.extension.clicks
import com.jradzik.mvicoroutines.extension.repeatOnStarted
import com.jradzik.mvicoroutines.extension.visibleWhen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DemoFragment : MVIFragment<DemoUserAction, DemoState, DemoSideEffect, DemoError>(),
    ViewBindingHolder<FragmentDemoBinding> by LifecycleViewBindingHolder() {

    override val viewModel: DemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bind(this, FragmentDemoBinding.inflate(inflater)) {
        setupActions()
    }

    private fun setupActions() = requireBinding {
        viewLifecycleOwner.repeatOnStarted {
            buttonReverseText.clicks().onEach {
                viewModel.dispatch(DemoUserAction.ReverseTextClicked)
            }.launchIn(this)

            buttonEnableReverse.clicks().onEach {
                viewModel.dispatch(DemoUserAction.EnableReverseTextClicked)
            }.launchIn(this)

            buttonReset.clicks().onEach {
                viewModel.dispatch(DemoUserAction.ResetClicked)
            }.launchIn(this)
        }
    }

    override fun render(viewState: DemoState) {
        with(requireBinding()) {
            textviewText.text = viewState.text
            progressBar.visibleWhen(viewState.isLoading)
            buttonReverseText.isEnabled = !viewState.isLoading && viewState.isReverseTextEnabled
            buttonEnableReverse.isEnabled = !viewState.isLoading
            buttonReset.isEnabled = !viewState.isLoading

            renderButtonEnableReverse(viewState)
        }
    }

    private fun renderButtonEnableReverse(viewState: DemoState) = requireBinding {
        if (viewState.isReverseTextEnabled) {
            buttonEnableReverse.text = getString(R.string.demo_disable_reverse)
            buttonEnableReverse.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            buttonEnableReverse.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded16_rectangle_red)
        } else {
            buttonEnableReverse.text = getString(R.string.demo_enable_reverse)
            buttonEnableReverse.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            buttonEnableReverse.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded16_rectangle_teal)
        }
    }

    override fun handleSideEffect(sideEffect: DemoSideEffect) {
        when (sideEffect) {
            is DemoSideEffect.ShowInfo -> showSnackbar(sideEffect.textResId, backgroundColorResId = R.color.grey)
            is DemoSideEffect.ShowSuccess -> showSnackbar(sideEffect.textResId, backgroundColorResId = R.color.teal_200)
        }
    }

    override fun handleError(error: DemoError) {
        showSnackbar(error.errorResId, R.color.red)
    }

    private fun showSnackbar(
        @StringRes textResId: Int,
        @ColorRes textColorResId: Int = R.color.black,
        @ColorRes backgroundColorResId: Int = R.color.grey
    ) = Snackbar.make(requireBinding().root, getString(textResId), Snackbar.LENGTH_LONG).apply {
        setTextColor(ContextCompat.getColor(requireContext(), textColorResId))
        setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundColorResId))
        show()
    }
}
