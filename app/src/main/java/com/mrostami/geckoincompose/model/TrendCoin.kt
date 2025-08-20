package com.mrostami.geckoincompose.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class TrendCoin(
    @SerialName("coin_id")
    @PrimaryKey var coinId: Int,
    @SerialName("id")
    var id: String?,
    @SerialName("large")
    var large: String?,
    @SerialName("market_cap_rank")
    var marketCapRank: Int?,
    @SerialName("name")
    var name: String?,
    @SerialName("price_btc")
    var priceBtc: Double?,
    @SerialName("score")
    var score: Double?,
    @SerialName("slug")
    var slug: String?,
    @SerialName("small")
    var small: String?,
    @SerialName("symbol")
    var symbol: String?,
    @SerialName("thumb")
    var thumb: String?
)