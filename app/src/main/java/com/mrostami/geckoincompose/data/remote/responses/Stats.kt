package com.mrostami.geckoincompose.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val total: Int,
    val total24hVolume: String,
    val totalCoins: Int,
    val totalExchanges: Int,
    val totalMarketCap: String,
    val totalMarkets: Int
)