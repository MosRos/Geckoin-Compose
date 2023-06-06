package com.mrostami.geckoincompose.ui.settings

import androidx.lifecycle.ViewModel
import com.mrostami.geckoincompose.domain.usecases.ThemeConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeConfigUseCase: ThemeConfigUseCase
) : ViewModel() {

}