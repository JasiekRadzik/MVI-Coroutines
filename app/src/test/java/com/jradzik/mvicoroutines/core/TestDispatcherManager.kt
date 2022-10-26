package com.jradzik.mvicoroutines.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
interface TestDispatcherManager {

    @Before
    fun setUpTestDispatcher()

    @After
    fun tearDownTestDispatcher() {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UnconfinedTestDispatcherManager : TestDispatcherManager {

    override fun setUpTestDispatcher() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class StandardTestDispatcherManager : TestDispatcherManager {

    override fun setUpTestDispatcher() {
        Dispatchers.setMain(StandardTestDispatcher())
    }
}
