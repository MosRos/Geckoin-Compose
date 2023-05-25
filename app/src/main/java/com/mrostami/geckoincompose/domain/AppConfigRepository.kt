package com.mrostami.geckoincompose.domain

import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    suspend fun changeTheme(mode: Int)
    suspend fun getThemeMode() : Flow<Int>
}