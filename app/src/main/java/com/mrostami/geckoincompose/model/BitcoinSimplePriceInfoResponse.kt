package com.mrostami.geckoincompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitcoinSimplePriceInfoResponse(
    @SerialName("bitcoin") val bitcoin: SimplePriceInfo?,
//    @SerializedName("ethereum") val ethereum: SimpleCoinInfo?
)