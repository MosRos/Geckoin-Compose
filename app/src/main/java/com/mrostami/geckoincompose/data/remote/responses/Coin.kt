package com.mrostami.geckoincompose.data.remote.responses

import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val `24hVolume`: String,
    val btcPrice: String,
    val change: String,
    val coinrankingUrl: String?,
    val color: String?,
    val iconUrl: String?,
    val isWrappedTrustless: Boolean?,
    val listedAt: Int?,
    val lowVolume: Boolean?,
    val marketCap: String,
    val name: String,
    val price: String,
    val rank: Int,
    val symbol: String,
    val tier: Int?,
    val uuid: String,
    val wrappedTo: String? = null
) {
    companion object {
        fun toRankedEntity(rankCoin: Coin) : RankedCoin = RankedCoin(
            id = rankCoin.uuid,
            currentPrice = rankCoin.price.toDoubleOrNull(),
            image = rankCoin.iconUrl,
            marketCap = rankCoin.marketCap.toLongOrNull(),
            marketCapChange24h = rankCoin.change.toDoubleOrNull(),
            marketCapRank = rankCoin.rank,
            name = rankCoin.name,
            priceChange24h = rankCoin.change.toDoubleOrNull(),
            priceChangePercentage24h = rankCoin.change.toDoubleOrNull(),
            symbol = rankCoin.symbol,
            totalVolume = rankCoin.`24hVolume`.toDoubleOrNull()
        )
    }
}