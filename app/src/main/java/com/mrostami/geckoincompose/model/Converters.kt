package com.mrostami.geckoincompose.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMCPercentagesList(value: List<MarketCapPercentageItem>?): String? {
        if (value == null) return null
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMCPercentagesList(value: String?): List<MarketCapPercentageItem>? {
        if (value == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<MarketCapPercentageItem>>() {}.type
        return gson.fromJson(value, type)
    }
}