package com.mrostami.geckoincompose.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mrostami.geckoincompose.model.MarketCapPercentageItem

class RoomConverters {
    @TypeConverter
    fun fromMarketCapsList(mCaps: MutableList<MarketCapPercentageItem>?) : String? {
        if (mCaps == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<MarketCapPercentageItem>>() {}.type
        return gson.toJson(mCaps, type)
    }

    @TypeConverter
    fun toMarketCapsList(mCapsString: String?) : MutableList<MarketCapPercentageItem>? {
        if (mCapsString.isNullOrEmpty()) return null
        val gson = Gson()
        val type = object : TypeToken<List<MarketCapPercentageItem>>() {}.type
        return gson.fromJson(mCapsString as? String, type)
    }

//    @TypeConverter
//    fun fromMCPercentagesList(value: List<MarketCapPercentageItem>?): String? {
//        if (value == null) return null
//        val gson = Gson()
//        return gson.toJson(value)
//    }
//
//    @TypeConverter
//    fun toMCPercentagesList(value: String?): List<MarketCapPercentageItem>? {
//        if (value == null) return null
//        val gson = Gson()
//        val type = object : TypeToken<List<MarketCapPercentageItem>>() {}.type
//        return gson.fromJson(value, type)
//    }
}