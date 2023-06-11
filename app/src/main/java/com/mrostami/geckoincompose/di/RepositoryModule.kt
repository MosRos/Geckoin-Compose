package com.mrostami.geckoincompose.di

import com.mrostami.geckoincompose.data.local.LocalDataSource
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.data.repositories.AllCoinsRepositoryImpl
import com.mrostami.geckoincompose.data.repositories.AppConfigRepositoryImpl
import com.mrostami.geckoincompose.data.repositories.GlobalInfoRepositoryImpl
import com.mrostami.geckoincompose.data.repositories.MarketRanksMediator
import com.mrostami.geckoincompose.data.repositories.MarketRanksRepositoryImpl
import com.mrostami.geckoincompose.data.repositories.NetworkMarketRanksPagingSource
import com.mrostami.geckoincompose.domain.AllCoinsRepository
import com.mrostami.geckoincompose.domain.AppConfigRepository
import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAppConfigRepository(
        localDataSource: LocalDataSource
    ) : AppConfigRepository {
        return AppConfigRepositoryImpl(localDataSource = localDataSource)
    }

    @Singleton
    @Provides
    fun provideGlobalInfoRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) : GlobalInfoRepository {
        return GlobalInfoRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Singleton
    @Provides
    fun provideMarketRanksRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        marketRanksMediator: MarketRanksMediator,
        networkMarketRanksPagingSource: NetworkMarketRanksPagingSource
    ) : MarketRanksRepository {
            return MarketRanksRepositoryImpl(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
                marketRanksMediator = marketRanksMediator,
                networkMarketRanksPagingSource
            )
    }

    @Singleton
    @Provides
    fun provideAllCoinsRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) : AllCoinsRepository {
        return AllCoinsRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }
}