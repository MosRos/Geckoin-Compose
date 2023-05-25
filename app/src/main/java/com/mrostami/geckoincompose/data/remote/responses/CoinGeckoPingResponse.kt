package com.mrostami.geckoin.data.remote.responses

import com.google.gson.annotations.SerializedName

data class CoinGeckoPingResponse(
    @SerializedName("gecko_says")
    var geckoSays: String? = null // (V3) To the Moon!
)