package com.mrostami.geckoincompose.data.remote.responses

import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.model.Roi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankCoin(
    @SerialName("id") val id: String,
    @SerialName("ath")
    val ath: Double?, // 1448.18
    @SerialName("ath_change_percentage")
    val athChangePercentage: Double?, // -89.18864
    @SerialName("ath_date")
    val athDate: String?, // 2018-01-13T00:00:00.000Z
    @SerialName("atl")
    val atl: Double?, // 0.432979
    @SerialName("atl_change_percentage")
    val atlChangePercentage: Double?, // 89894.52683
    @SerialName("atl_date")
    val atlDate: String?, // 2015-10-20T00:00:00.000Z
    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: Long,
    @SerialName("circulating_supply")
    val circulatingSupply: Double?, // 110463081.8115
    @SerialName("current_price")
    val currentPrice: Double?, // 156.44
    @SerialName("high_24h")
    val high24h: Double?, // 170.85
    @SerialName("image")
    val image: String?, // https://assets.coingecko.com/coins/images/279/large/ethereum.png?1547034048
    @SerialName("last_updated")
    val lastUpdated: String?, // 2020-04-10T18:27:39.161Z
    @SerialName("low_24h")
    val low24h: Double?, // 153.71
    @SerialName("market_cap")
    val marketCap: Long?, // 17294979690
    @SerialName("market_cap_change_24h")
    val marketCapChange24h: Double?, // -1441856599.09626
    @SerialName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double?, // -7.6953
    @SerialName("market_cap_rank")
    val marketCapRank: Int?, // 2
    @SerialName("name")
    val name: String?, // Ethereum
    @SerialName("price_change_24h")
    val priceChange24h: Double?, // -13.44174154
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?, // -7.91249
    @SerialName("roi") val roi: Roi?,
    @SerialName("symbol")
    val symbol: String?, // eth
    @SerialName("total_supply")
    val totalSupply: Double?, // null
    @SerialName("max_supply")
    val maxSupply: Double?,
    @SerialName("total_volume")
    val totalVolume: Double?, // 14969737842
) {
    companion object {
        fun toRankedEntity(rankCoin: RankCoin) : RankedCoin = RankedCoin(
            id = rankCoin.id,
            ath = rankCoin.ath,
            athChangePercentage = rankCoin.athChangePercentage,
            athDate = rankCoin.athDate,
            atl = rankCoin.atl,
            atlChangePercentage = rankCoin.atlChangePercentage,
            atlDate = rankCoin.atlDate,
            circulatingSupply = rankCoin.circulatingSupply,
            currentPrice = rankCoin.currentPrice,
            high24h = rankCoin.high24h,
            image = rankCoin.image,
            lastUpdated = rankCoin.lastUpdated,
            low24h = rankCoin.low24h,
            marketCap = rankCoin.marketCap,
            marketCapChange24h = rankCoin.marketCapChange24h,
            marketCapChangePercentage24h = rankCoin.marketCapChangePercentage24h,
            marketCapRank = rankCoin.marketCapRank,
            name = rankCoin.name,
            priceChange24h = rankCoin.priceChange24h,
            priceChangePercentage24h = rankCoin.priceChangePercentage24h,
            roi = rankCoin.roi,
            symbol = rankCoin.symbol,
            totalSupply = rankCoin.totalSupply,
            totalVolume = rankCoin.totalVolume
        )
    }
}

@Serializable
data class Roi2(
    @SerialName("currency")
    val currency: String?,
    @SerialName("percentage")
    val percentage: Double?,
    @SerialName("times")
    val times: Double?
)
