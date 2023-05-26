package com.mrostami.geckoincompose.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class TrendCoin(
    @SerialName("coin_id")
    @PrimaryKey val coinId: Int,
    @SerialName("id")
    val id: String?,
    @SerialName("large")
    val large: String?,
    @SerialName("market_cap_rank")
    val marketCapRank: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("price_btc")
    val priceBtc: Double?,
    @SerialName("score")
    val score: Double?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("small")
    val small: String?,
    @SerialName("symbol")
    val symbol: String?,
    @SerialName("thumb")
    val thumb: String?
)