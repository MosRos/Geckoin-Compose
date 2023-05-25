package com.mrostami.geckoincompose.data.local.dao

import kotlinx.coroutines.flow.Flow

interface PreferencesDao {

    suspend fun changeTheme(mode: Int)
    suspend fun getThemeMode() : Flow<Int>

    fun setAuthToken(token: String)
    fun getAuthToken() : String?

    fun setLastSyncDate(time: Long)
    fun getLastSyncDate() : Long
}