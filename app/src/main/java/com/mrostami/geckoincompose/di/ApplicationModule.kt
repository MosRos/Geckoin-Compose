package com.mrostami.geckoincompose.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mrostami.geckoincompose.data.local.CryptoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private const val PREFS_MAME = "geckoin_app_prefs"
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(PREFS_MAME, Context.MODE_PRIVATE)
    }

    private val Context.dataStore by preferencesDataStore(PREFS_MAME)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : CryptoDataBase {
        return CryptoDataBase.getInstance(context)
    }

    @Provides
    fun provideGson() : Gson {
        return GsonBuilder().create()
    }
}