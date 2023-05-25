package com.mrostami.geckoincompose.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class TrendCoin(
    @SerializedName("coin_id")
    @PrimaryKey val coinId: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("large")
    val large: String?,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price_btc")
    val priceBtc: Double?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("thumb")
    val thumb: String?
)