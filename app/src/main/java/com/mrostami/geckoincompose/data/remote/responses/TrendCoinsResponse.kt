package com.mrostami.geckoin.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.mrostami.geckoincompose.model.TrendCoin

data class TrendCoinsResponse(
    @SerializedName("coins")
    val coinItems: List<CoinItem>?
)

data class CoinItem(
    @SerializedName("item")
    val item: TrendCoin?
)