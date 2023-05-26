package com.mrostami.geckoin.data.remote.responses

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGeckoPingResponse(
    @SerialName("gecko_says")
    var geckoSays: String? = null // (V3) To the Moon!
)