package com.mrostami.geckoincompose.ui.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.usecases.ThemeConfigUseCase
import com.mrostami.geckoincompose.model.ThemeMode
import com.mrostami.geckoincompose.model.toThemeMode
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeConfigUseCase: ThemeConfigUseCase
) : ViewModel() {
    private var _uiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState.initialState)
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        observeThemeMode()
    }

    fun onNewEvent(event: SettingsEvents) {
        viewModelScope.launch {
            reduce(event = event, oldState = _uiState.value)
        }
    }

    private fun observeThemeMode() {
        viewModelScope.launch {
            themeConfigUseCase.getThemeMode().collectLatest { id ->
                _uiState.emit(
                    _uiState.value.copy(
                        data = _uiState.value.data.copy(
                            themeMode = id.toThemeMode()
                        )
                    )
                )
            }
        }
    }

    private suspend fun reduce(event: SettingsEvents, oldState: SettingsUiState) {
        when (event) {
            is SettingsEvents.ChangeTheme -> {
               themeConfigUseCase.changeTheme(event.mode.id)
            }
        }
    }

    @Stable
    data class SettingsUiData(
        val themeMode: ThemeMode = ThemeMode.AUTO,
        val developerAddress: String = "https://www.linkedin.com/in/ali-rostami-aa3929165/",
        val aboutRepoAddress: String = "https://github.com/MosRos/Geckoin-Compose"
    )

    sealed interface SettingsEffects

    sealed interface SettingsEvents {
        data class ChangeTheme(val mode: ThemeMode) : SettingsEvents
    }

    @Immutable
    data class SettingsUiState(
        override val state: BaseUiState.State = BaseUiState.State.LOADING,
        override val errorMessage: String? = null,
        override val data: SettingsUiData = SettingsUiData()
    ) : BaseUiState {
        companion object {
            val initialState = SettingsUiState()
        }
    }
}


