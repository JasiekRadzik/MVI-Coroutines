package com.jradzik.mvicoroutines.core.viewbinding

/**
 * Copied and modified from the source below:
 * https://gist.github.com/Frank1234/94211000cae3ec7652442bbc18172f25#file-viewbindingholder-kt
 */

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<T : ViewBinding> {
    val binding: T?

    fun bind(fragment: Fragment, binding: T, onBound: (T.() -> Unit)?): View

    fun requireBinding(block: (T.() -> Unit)? = null): T
}

class LifecycleViewBindingHolder<T : ViewBinding> : ViewBindingHolder<T>, LifecycleEventObserver {

    override var binding: T? = null
    private var lifecycle: Lifecycle? = null

    private lateinit var fragmentName: String

    override fun bind(fragment: Fragment, binding: T, onBound: (T.() -> Unit)?): View {
        this.binding = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle.apply {
            addObserver(this@LifecycleViewBindingHolder)
        }
        fragmentName = fragment::class.simpleName ?: "Unknown"
        onBound?.invoke(binding)
        return binding.root
    }

    override fun requireBinding(block: (T.() -> Unit)?): T {
        val binding = binding
        check(binding != null) {
            "Accessing binding outside of Fragment lifecycle: $fragmentName, Current state: ${lifecycle?.currentState}"
        }
        block?.invoke(binding)
        return binding
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                lifecycle?.removeObserver(this)
                lifecycle = null
                binding = null
            }
            else -> {}
        }
    }
}
