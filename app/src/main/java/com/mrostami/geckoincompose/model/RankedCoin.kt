package com.mrostami.geckoincompose.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
data class RankedCoin(
    @SerializedName("id")
    @PrimaryKey @ColumnInfo(name = "id") var id: String = "0xxx",
    @SerializedName("ath")
    var ath: Double? = null, // 1448.18
    @SerializedName("ath_change_percentage")
    var athChangePercentage: Double? = null, // -89.18864
    @SerializedName("ath_date")
    var athDate: String? = null, // 2018-01-13T00:00:00.000Z
    @SerializedName("atl")
    var atl: Double? = null, // 0.432979
    @SerializedName("atl_change_percentage")
    var atlChangePercentage: Double? = null, // 89894.52683
    @SerializedName("atl_date")
    var atlDate: String? = null, // 2015-10-20T00:00:00.000Z

    @SerializedName("circulating_supply")
    var circulatingSupply: Double? = null, // 110463081.8115
    @SerializedName("current_price")
    var currentPrice: Double? = null, // 156.44
    @SerializedName("high_24h")
    var high24h: Double? = null, // 170.85
    @SerializedName("image")
    var image: String? = null, // https://assets.coingecko.com/coins/images/279/large/ethereum.png?1547034048
    @SerializedName("last_updated")
    var lastUpdated: String? = null, // 2020-04-10T18:27:39.161Z
    @SerializedName("low_24h")
    var low24h: Double? = null, // 153.71
    @SerializedName("market_cap")
    var marketCap: Long? = null, // 17294979690
    @SerializedName("market_cap_change_24h")
    var marketCapChange24h: Double? = null, // -1441856599.09626
    @SerializedName("market_cap_change_percentage_24h")
    var marketCapChangePercentage24h: Double? = null, // -7.6953
    @SerializedName("market_cap_rank")
    var marketCapRank: Int? = null, // 2
    @SerializedName("name")
    var name: String? = null, // Ethereum
    @SerializedName("price_change_24h")
    var priceChange24h: Double? = null, // -13.44174154
    @SerializedName("price_change_percentage_24h")
    var priceChangePercentage24h: Double? = null, // -7.91249
    @SerializedName("roi")
    @Expose(deserialize = false)
    @Ignore var roi: Roi? = null,
    @SerializedName("symbol")
    var symbol: String? = null, // eth
    @SerializedName("total_supply")
    var totalSupply: Double? = null, // null
    @SerializedName("total_volume")
    var totalVolume: Double? = null, // 14969737842
)