package com.mrostami.geckoincompose.ui.base

import com.mrostami.geckoincompose.BuildConfig
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

abstract class StateMachine<S : BaseUiState, E : BaseUiEvent, F: BaseUiEffect>(initialState: S) {

    var state: MutableStateFlow<S> = MutableStateFlow(initialState)
        private set

    var effects: MutableSharedFlow<F> = MutableSharedFlow()
        private set

    // Time Capsule is optional, its useful for debugging
    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        state.tryEmit(storedState)
    }

    init {
        timeCapsule.addState(initialState)
    }

    fun sendEvent(event: E) {
        reduce(event, state.value)
    }

    fun emitEffect(effect: F) {
        val success = effects.tryEmit(effect)
        if (BuildConfig.DEBUG && success) {
            Timber.d("Effect emitted ${effect.toString()}")
        }
    }

    fun updateState(newState: S) {
        val success = state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }
    }

    abstract fun reduce(event: E, oldState: S)
}

//interface StateMachineInterface<S : BaseUiState, E : BaseUiEvent, F: BaseUiEffect> {
//
//    var initialState: S
//
//    val state: MutableStateFlow<S>
//        get() = MutableStateFlow(initialState)
//        private set
//
//    val effects: MutableSharedFlow<F>
//        get() = MutableSharedFlow()
//        private set
//
//    // Time Capsule is optional, its useful for debugging
//    val timeCapsule: TimeCapsule<S>
//        get() = TimeTravelCapsule { storedState ->
//            state.tryEmit(storedState)
//        }
//
//    fun sendEvent(event: E) {
//        reduce(event, state.value)
//    }
//
//    fun emitEffect(effect: F) {
//        val success = effects.tryEmit(effect)
//        if (BuildConfig.DEBUG && success) {
//            Timber.d("Effect emitted ${effect.toString()}")
//        }
//    }
//
//    fun updateState(newState: S) {
//        val success = state.tryEmit(newState)
//
//        if (BuildConfig.DEBUG && success) {
//            timeCapsule.addState(newState)
//        }
//    }
//
//    fun reduce(event: E, oldState: S)
//}