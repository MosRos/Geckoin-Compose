package com.mrostami.geckoincompose.model


import com.google.gson.annotations.SerializedName

data class SimplePriceInfo(
    @SerializedName("last_updated_at")
    val lastUpdatedAt: Long?,
    @SerializedName("usd")
    val usd: Double?,
    @SerializedName("usd_24h_change")
    val usd24hChange: Double?,
    @SerializedName("usd_24h_vol")
    val usd24hVol: Double?,
    @SerializedName("usd_market_cap")
    val usdMarketCap: Double?,
//    @Expose(serialize = false, deserialize = false)
//    @PrimaryKey val id: String = "bitcoin"
)