package com.mrostami.geckoin.data.remote.responses


import com.google.gson.annotations.SerializedName

data class PriceChartResponse(
    @SerializedName("prices")
    val prices: List<List<Double>>?
)