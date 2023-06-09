package com.mrostami.geckoincompose.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<S: BaseUiState, in E: BaseUiEvent> : ViewModel() {
    abstract val uiState: Flow<S>
}