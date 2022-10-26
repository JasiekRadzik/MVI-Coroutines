package com.jradzik.mvicoroutines.extension

import android.view.View
import androidx.annotation.CheckResult
import dagger.hilt.android.internal.ThreadUtil.ensureMainThread
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

@CheckResult
fun View.clicks(): Flow<Unit> = callbackFlow {
    ensureMainThread()

    setOnClickListener {
        trySend(Unit)
    }

    awaitClose { setOnClickListener(null) }
}.conflate()
