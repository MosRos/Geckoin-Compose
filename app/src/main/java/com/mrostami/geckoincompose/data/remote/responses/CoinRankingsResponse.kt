package com.mrostami.geckoincompose.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class CoinRankingsResponse(
    val `data`: Data? = null,
    val status: String,
    val code: String? = null,
    val message: String? = null
)