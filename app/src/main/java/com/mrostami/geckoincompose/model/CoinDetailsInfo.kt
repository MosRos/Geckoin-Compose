package com.mrostami.geckoincompose.model


data class CoinDetailsInfo(
    var id: String? = null, // bitcoin
    var symbol: String? = null, // btc
    var name: String? = null, // Bitcoin
    var marketCapRank: Int? = null, // 1
    var imageUrl: String? = null,
    var description: String? = null,
    var hashingAlgorithm: String? = null, // SHA-256
    var lastUpdated: String? = null, // 2020-07-25T08:08:09.170Z
    var sentimentVotesDownPercentage: Double? = null, // 27.8
    var sentimentVotesUpPercentage: Double? = null, // 72.2
    var categories: List<String>? = null,
    var coingeckoRank: Int? = null, // 6
    var coingeckoScore: Double? = null, // 85.878
    var communityScore: Double? = null, // 73.457
    var developerScore: Double? = null, // 98.829
)
