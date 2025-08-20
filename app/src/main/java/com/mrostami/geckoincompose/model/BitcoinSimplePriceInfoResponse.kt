package com.mrostami.geckoincompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitcoinSimplePriceInfoResponse(
    @SerialName("bitcoin") var bitcoin: SimplePriceInfo?,
//    @SerializedName("ethereum") val ethereum: SimpleCoinInfo?
)