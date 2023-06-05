package com.mrostami.geckoincompose.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimplePriceInfo(
    @SerialName("last_updated_at")
    val lastUpdatedAt: Long? = 0L,
    @SerialName("usd")
    val usd: Double? = 0.0,
    @SerialName("usd_24h_change")
    val usd24hChange: Double? = 0.0,
    @SerialName("usd_24h_vol")
    val usd24hVol: Double? = 0.0,
    @SerialName("usd_market_cap")
    val usdMarketCap: Double? = 0.0,
//    @Expose(serialize = false, deserialize = false)
//    @PrimaryKey val id: String = "bitcoin"
)