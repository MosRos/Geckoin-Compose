package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeConfigUseCase @Inject constructor(
    private val appConfigRepository: AppConfigRepository
) {
    suspend fun getThemeMode() : Flow<Int> = appConfigRepository.getThemeMode()
    suspend fun changeTheme(mode: Int) = appConfigRepository.changeTheme(mode)
}