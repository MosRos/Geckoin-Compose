package com.mrostami.geckoincompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Roi(
    @SerialName("currency")
    var currency: String? = null, // btc
    @SerialName("percentage")
    var percentage: Double? = null, // 2943.3353518814183
    @SerialName("times")
    var times: Double? = null // 29.433353518814183
)