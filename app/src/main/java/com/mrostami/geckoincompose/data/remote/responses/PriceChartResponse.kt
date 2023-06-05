package com.mrostami.geckoin.data.remote.responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceChartResponse(
    @SerialName("prices")
    val prices: List<List<Double>>?
)