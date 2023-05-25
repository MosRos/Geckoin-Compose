package com.mrostami.geckoincompose.model

import com.google.gson.annotations.SerializedName

data class Roi(
    @SerializedName("currency")
    var currency: String? = null, // btc
    @SerializedName("percentage")
    var percentage: Double? = null, // 2943.3353518814183
    @SerializedName("times")
    var times: Double? = null // 29.433353518814183
)