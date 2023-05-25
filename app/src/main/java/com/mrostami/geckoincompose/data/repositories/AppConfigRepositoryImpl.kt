package com.mrostami.geckoincompose.data.repositories

import com.mrostami.geckoincompose.data.local.LocalDataSource
import com.mrostami.geckoincompose.domain.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : AppConfigRepository {

    override suspend fun changeTheme(mode: Int) {
        localDataSource.changeTheme(mode)
    }

    override suspend fun getThemeMode(): Flow<Int> {
        return flow {
            emitAll(localDataSource.getThemeMode())
        }
    }
}