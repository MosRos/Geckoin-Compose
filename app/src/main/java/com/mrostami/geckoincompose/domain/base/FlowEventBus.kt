package com.mrostami.geckoincompose.domain.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/***
 * See Link https://github.com/Kotlin/kotlinx.coroutines/issues/2034
 */
class FlowEventBus {
    private val _events = MutableSharedFlow<Any>() // no buffer, rendezvous with subscribers
    val events: Flow<Any> get() = _events // expose as a plain flow

    suspend fun produceEvent(event: Any) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}