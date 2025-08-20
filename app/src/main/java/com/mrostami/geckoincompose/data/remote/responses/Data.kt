package com.mrostami.geckoincompose.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val coins: List<Coin>,
    val stats: Stats
)