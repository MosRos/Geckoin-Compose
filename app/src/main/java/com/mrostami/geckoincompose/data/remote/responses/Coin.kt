package com.mrostami.geckoincompose.data.remote.responses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.serialization.Serializable

@Serializable
@Entity
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
    @PrimaryKey @ColumnInfo(name = "id") val uuid: String,
    val wrappedTo: String? = null,
    @Expose(serialize = false, deserialize = false)
    var pageKey: Int? = null
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