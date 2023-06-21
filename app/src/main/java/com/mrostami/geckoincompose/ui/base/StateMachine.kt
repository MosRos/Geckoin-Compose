package com.mrostami.geckoincompose.ui.base

import com.mrostami.geckoincompose.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class StateMachine<S : BaseUiState, E : BaseUiEvent, F: BaseUiEffect>(initialState: S) {

    var state: MutableStateFlow<S> = MutableStateFlow(initialState)
        private set

    private var effectChannel: Channel<F> = Channel()
    val effect: Flow<F> = effectChannel.receiveAsFlow()

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

    fun setState(newState: S) {
        val success = state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }
    }

    abstract fun reduce(event: E, oldState: S)
}