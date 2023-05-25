package com.mrostami.geckoincompose.model

import com.google.gson.annotations.SerializedName

data class BitcoinSimplePriceInfoResponse(
    @SerializedName("bitcoin") val bitcoin: SimplePriceInfo?,
//    @SerializedName("ethereum") val ethereum: SimpleCoinInfo?
)