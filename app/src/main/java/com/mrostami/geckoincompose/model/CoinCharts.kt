/*
 * *
 *  * Created by Moslem Rostami on 8/2/20 11:40 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 7/27/20 9:40 PM
 *
 */

package com.mrostami.geckoincompose.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketCapPercentageItem(
 @PrimaryKey
 @ColumnInfo(name = "id") var coinId: String,
 var cap: Double
)

@Entity(tableName = "CoinPrice")
data class CoinPriceEntity(
 @PrimaryKey
 @ColumnInfo(name = "id") var coinId: String,
 var daysPeriod: Int,
 var time: Long,
 var prc: Double
)

@Entity(tableName = "CoinMarketCaps")
data class CoinMarketCapsEntity(
 @PrimaryKey
 @ColumnInfo(name = "id") var coinId: String,
 var daysPeriod: Int,
 var time: Long,
 var cap: Double
)

@Entity(tableName = "CoinTotalVolume")
data class CoinTotalVolumeEntity(
 @PrimaryKey
 @ColumnInfo(name = "id") var coinId: String,
 var daysPeriod: Int,
 var time: Long,
 var volume: Double
)