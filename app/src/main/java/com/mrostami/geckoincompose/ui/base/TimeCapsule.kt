package com.mrostami.geckoincompose.ui.base

interface TimeCapsule<S : BaseUiState> {
    fun addState(state: S)
    fun selectState(position: Int)
    fun getStates(): List<S>
}

class TimeTravelCapsule<S : BaseUiState>(
    private val onStateSelected: (S) -> Unit
) : TimeCapsule<S> {

    private val states = mutableListOf<S>()

    override fun addState(state: S) {
        states.add(state)
    }

    override fun selectState(position: Int) {
        onStateSelected(states[position])
    }

    override fun getStates(): List<S> {
        return states
    }
}