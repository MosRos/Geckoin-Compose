package com.mrostami.geckoincompose.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimplePriceInfo(
    @SerialName("last_updated_at")
    var lastUpdatedAt: Long? = 0L,
    @SerialName("usd")
    var usd: Double? = 0.0,
    @SerialName("usd_24h_change")
    var usd24hChange: Double? = 0.0,
    @SerialName("usd_24h_vol")
    var usd24hVol: Double? = 0.0,
    @SerialName("usd_market_cap")
    var usdMarketCap: Double? = 0.0,
//    @Expose(serialize = false, deserialize = false)
//    @PrimaryKey val id: String = "bitcoin"
)