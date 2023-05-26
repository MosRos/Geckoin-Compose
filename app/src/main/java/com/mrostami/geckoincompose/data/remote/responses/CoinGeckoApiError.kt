package com.mrostami.geckoin.data.remote.responses

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class CoinGeckoApiError(
    @SerialName("error")
    var error: String? = "invalid"
)