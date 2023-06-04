package com.mrostami.geckoincompose.ui.base

interface BaseUiModel {
    enum class State {
        LOADING,
        ERROR,
        SUCCESS
    }
    val state: State
    val errorMessage: String?
    val data: Any
}