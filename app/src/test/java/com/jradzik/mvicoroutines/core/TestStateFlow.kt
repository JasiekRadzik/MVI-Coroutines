package com.jradzik.mvicoroutines.core

import com.jradzik.mvicoroutines.core.mvi.MVIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent

@OptIn(ExperimentalCoroutinesApi::class)
interface TestStateFlow {

    /**
     * TestStateFlow allows to easily test StateFlows values
     * @param testScope represents current tests scope
     * @param testSubject is an MVIState, that allows to access the StateFlow in the MVIViewModel
     * @param tasksToRun allows to schedule tasks before the testScope.runCurrent runs them
     * @param post provides a list that contains of all the results that went through the StateFlow
     * Protip: results: List<T> contains an initial state at index = 0
     * Turbine - library to test StateFlow
     */
    fun <T : Any> testStateFlow(testScope: TestScope, testSubject: MVIState<T>, tasksToRun: () -> Unit, post: (results: List<T>) -> Unit)
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestStateFlowImpl : TestStateFlow {
    override fun <T : Any> testStateFlow(testScope: TestScope, testSubject: MVIState<T>, tasksToRun: () -> Unit, post: (results: List<T>) -> Unit) {
        val results = mutableListOf<T>()
        val job = testScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            testSubject.state.toList(results)
        }
        tasksToRun()
        testScope.runCurrent()
        post(results)
        job.cancel()
    }
}
