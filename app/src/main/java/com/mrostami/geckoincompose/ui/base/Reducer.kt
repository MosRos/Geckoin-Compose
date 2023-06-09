package com.mrostami.geckoincompose.ui.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class Reducer<S : BaseUiState, E : BaseUiEvent, F: BaseUiEffect>(initialState: S) {

    var state: MutableStateFlow<S> = MutableStateFlow(initialState)
        private set

    var effect: Channel<F> = Channel()
        private set

    // Time Capsule is optional, its useful for debugging
//    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
//        state.tryEmit(storedState)
//    }

    init {
//        timeCapsule.addState(initialState)
    }

    fun sendEvent(event: E) {
        reduce(event, state.value)
    }

    fun setState(newState: S) {
        val success = state.tryEmit(newState)

//        if (BuildConfig.DEBUG && success) {
//            timeCapsule.addState(newState)
//        }
    }

    abstract fun reduce(event: E, oldState: S)
}