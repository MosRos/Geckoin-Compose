package com.mrostami.geckoincompose.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity
data class GlobalMarketInfo(
    @PrimaryKey
    var id: Int = 0,
    var activeCryptocurrencies: Int = 0,
    var markets: Int = 0,
    var endedIcos: Int = 0,
    var ongoingIcos: Int = 0,
    var upcomingIcos: Int = 0,
    var marketCapChangePercentage24hUsd: Double = 0.0,
    val marketCapPercentages: List<MarketCapPercentageItem> = emptyList(),
    var updatedAt: Long = 0
)