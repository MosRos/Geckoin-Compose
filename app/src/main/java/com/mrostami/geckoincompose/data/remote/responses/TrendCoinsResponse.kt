package com.mrostami.geckoin.data.remote.responses

import com.mrostami.geckoincompose.model.TrendCoin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendCoinsResponse(
    @SerialName("coins")
    val coinItems: List<CoinItem>?,
//    @SerialName("exchanges")
//    val exchanges: List<Coin>?
)

@Serializable
data class CoinItem(
    @SerialName("item")
    val item: TrendCoin?
)