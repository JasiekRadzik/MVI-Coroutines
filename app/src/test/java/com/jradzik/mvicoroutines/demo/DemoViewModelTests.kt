package com.jradzik.mvicoroutines.demo

import com.google.common.truth.Truth
import com.jradzik.mvicoroutines.domain.core.MVICoroutinesResult
import com.jradzik.mvicoroutines.domain.feature.demo.ReverseTextRequestModel
import com.jradzik.mvicoroutines.domain.feature.demo.ReverseTextUseCase
import com.jradzik.mvicoroutines.R
import com.jradzik.mvicoroutines.core.StandardTestDispatcherManager
import com.jradzik.mvicoroutines.core.TestDispatcherManager
import com.jradzik.mvicoroutines.core.TestStateFlow
import com.jradzik.mvicoroutines.core.TestStateFlowImpl
import com.jradzik.mvicoroutines.feature.demo.DemoViewModel
import com.jradzik.mvicoroutines.feature.demo.DemoUserAction
import com.jradzik.mvicoroutines.feature.demo.DemoState
import com.jradzik.mvicoroutines.feature.demo.DemoError
import com.jradzik.mvicoroutines.feature.demo.DemoSideEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest :
    TestDispatcherManager by StandardTestDispatcherManager(),
    TestStateFlow by TestStateFlowImpl() {

    @Mock
    lateinit var reverseTextUseCase: ReverseTextUseCase

    private lateinit var testSubject: DemoViewModel

    private val defaultState = DemoState()

    @Before
    fun setUp() {
        testSubject = DemoViewModel(reverseTextUseCase)
    }

    @Test
    fun `Given DemoUserAction_ReverseTextClicked, Then update state with proper values`() = runTest {
        // Given
        val action = DemoUserAction.ReverseTextClicked
        val expectedState = defaultState.copy(isLoading = true)

        `when`(reverseTextUseCase(ReverseTextRequestModel(defaultState.text))).thenReturn(
            MVICoroutinesResult.Error()
        )

        testStateFlow(
            testScope = this,
            testSubject = testSubject,
            tasksToRun = {
                testSubject.dispatch(action)
            },
            post = { results ->
                Truth.assertThat(results.size).isEqualTo(3)
                Truth.assertThat(results[1]).isEqualTo(expectedState)
            }
        )
    }

    @Test
    fun `Given DemoUserAction_ReverseTextClicked, When reverseTextUseCase returns an Error, Then update state with proper values`() = runTest {
        // Given
        val action = DemoUserAction.ReverseTextClicked
        val expectedState = defaultState.copy(isLoading = false)

        `when`(reverseTextUseCase(ReverseTextRequestModel(defaultState.text))).thenReturn(
            MVICoroutinesResult.Error()
        )

        testStateFlow(
            testScope = this,
            testSubject = testSubject,
            tasksToRun = {
                testSubject.dispatch(action)
            },
            post = { results ->
                Truth.assertThat(results.size).isEqualTo(3)
                Truth.assertThat(results[2]).isEqualTo(expectedState)
            }
        )
    }

    @Test
    fun `Given DemoUserAction_ReverseTextClicked, When reverseTextUseCase returns an Error, Then emit DemoError error`() = runTest {
        // Given
        val action = DemoUserAction.ReverseTextClicked
        val expectedSideEffect = DemoError(R.string.demo_reverse_text_error)

        `when`(reverseTextUseCase(ReverseTextRequestModel(defaultState.text))).thenReturn(
            MVICoroutinesResult.Error()
        )
        // When
        testSubject.dispatch(action)

        // Then
        val result = testSubject.errors.firstOrNull()
        Truth.assertThat(result).isEqualTo(expectedSideEffect)
    }

    @Test
    fun `Given DemoUserAction_ReverseTextClicked, When reverseTextUseCase returns an Ok, Then update state with proper values`() = runTest {
        // Given
        val action = DemoUserAction.ReverseTextClicked
        val textReversed = "Text Reversed xd"
        val expectedState = defaultState.copy(isLoading = false, text = textReversed)

        `when`(reverseTextUseCase(ReverseTextRequestModel(defaultState.text))).thenReturn(
            MVICoroutinesResult.Ok(textReversed)
        )

        testStateFlow(
            testScope = this,
            testSubject = testSubject,
            tasksToRun = {
                testSubject.dispatch(action)
            },
            post = { results ->
                Truth.assertThat(results.size).isEqualTo(3)
                Truth.assertThat(results[2]).isEqualTo(expectedState)
            }
        )
    }

    @Test
    fun `Given DemoUserAction_ReverseTextClicked, When reverseTextUseCase returns an Ok, Then emit ShowSuccess side effect`() = runTest {
        // Given
        val action = DemoUserAction.ReverseTextClicked
        val expectedSideEffect = DemoSideEffect.ShowSuccess(R.string.demo_reverse_text_success)

        `when`(reverseTextUseCase(ReverseTextRequestModel(defaultState.text))).thenReturn(
            MVICoroutinesResult.Ok("Text reversed")
        )
        // When
        testSubject.dispatch(action)

        // Then
        val result = testSubject.sideEffects.firstOrNull()
        Truth.assertThat(result).isEqualTo(expectedSideEffect)
    }

    @Test
    fun `Given DemoUserAction_ResetClicked, Then update state with proper values`()  = runTest {
        // Given
        val action = DemoUserAction.ResetClicked
        val expectedState = defaultState.copy()

        // When
        testSubject.dispatch(action)

        // Then
        val result = testSubject.state.firstOrNull()
        Truth.assertThat(result).isEqualTo(expectedState)
    }

    @Test
    fun `Given DemoUserAction_ResetClicked, Then emit ShowInfo side effect`()  = runTest {
        // Given
        val action = DemoUserAction.ResetClicked
        val expectedSideEffect = DemoSideEffect.ShowInfo(R.string.demo_reset_info)

        // When
        testSubject.dispatch(action)

        // Then
        val result = testSubject.sideEffects.firstOrNull()
        Truth.assertThat(result).isEqualTo(expectedSideEffect)
    }
}