package com.mrostami.geckoincompose.data.local.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import javax.inject.Inject

class PreferencesHelper @Inject constructor(
    private val preferences: SharedPreferences,
    private val gson: Gson
    ) {

    // Pairs of preferences key_name and default value
    private val themeModePair = Pair("theme_mode", 0)
    private val authTokenPair = Pair<String, String?>("auth_token", null)
    private val globalMarketPair = Pair<String, String?>("global_market_info", null)
    private val lastSyncDate = Pair<String, Long>("last_sync_date", 0)


    var selectedThemeMode: Int
        get() = preferences.getInt(themeModePair.first, themeModePair.second)
        set(value) = preferences.edit {
            putInt(themeModePair.first, value)
        }

    var authToken: String?
        get() = preferences.getString(authTokenPair.first, authTokenPair.second)
        set(value) = preferences.edit {
            putString(authTokenPair.first, value)
        }


    fun saveGlobalMarketEntity(globalMarketEntity: GlobalMarketInfo?) {
        val globalInfoJson: String? = gson.toJson(globalMarketEntity)
        preferences.edit {
            putString(globalMarketPair.first, globalInfoJson)
        }
    }

    fun getGlobalMarketEntity() : GlobalMarketInfo? {
        val jsonString: String? =
            preferences.getString(globalMarketPair.first, globalMarketPair.second)
        return try {
            gson.fromJson(jsonString, GlobalMarketInfo::class.java)
        } catch (e: Exception) {
            null
        }
    }

    var lastDbSyncDate: Long
        get() = preferences.getLong(lastSyncDate.first, lastSyncDate.second)
        set(value) = preferences.edit {
            putLong(lastSyncDate.first, value)
        }
}