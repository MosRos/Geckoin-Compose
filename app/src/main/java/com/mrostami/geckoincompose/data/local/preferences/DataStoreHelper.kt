/*
 * *
 *  * Created by Moslem Rostami on 5/31/21 8:04 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 5/31/21 8:04 PM
 *
 */

package com.mrostami.geckoincompose.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject


class DataStoreHelper @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // Pairs of preferences key_name and default value
    private val themeModePair = Pair("theme_mode", 0)

    fun <T> DataStore<Preferences>.getValueFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
        this.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun getThemeMode() : Flow<Int> {
        val prefKeyName = intPreferencesKey(themeModePair.first)
        val defaultValue: Int = themeModePair.second
        return dataStore.getValueFlow(prefKeyName, defaultValue) ?: flowOf(defaultValue)
    }

    suspend fun changeTheme(mode: Int) {
        val prefKeyName = intPreferencesKey(themeModePair.first)
        dataStore.setValue(prefKeyName, mode)
    }
}