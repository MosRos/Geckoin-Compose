package com.mrostami.geckoin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CoinDetailResponse(

    @SerializedName("id")
    var id: String? = null, // bitcoin

    @SerializedName("symbol")
    var symbol: String? = null, // btc

    @SerializedName("name")
    var name: String? = null, // Bitcoin

    @SerializedName("market_cap_rank")
    var marketCapRank: Int? = null, // 1

    @SerializedName("liquidity_score")
    var liquidityScore: Double? = null, // 100.052

    @SerializedName("image")
    var image: Image? = null,

    @SerializedName("description")
    var description: Description? = null,

    @SerializedName("hashing_algorithm")
    var hashingAlgorithm: String? = null, // SHA-256

    @SerializedName("country_origin")
    var countryOrigin: String? = null,

    @SerializedName("last_updated")
    var lastUpdated: String? = null, // 2020-07-25T08:08:09.170Z

    @SerializedName("sentiment_votes_down_percentage")
    var sentimentVotesDownPercentage: Double? = null, // 27.8
    @SerializedName("sentiment_votes_up_percentage")
    var sentimentVotesUpPercentage: Double? = null, // 72.2

    @SerializedName("links")
    var links: Links? = null,

    @SerializedName("asset_platform_id")
    @Expose(deserialize = false)
    var assetPlatformId: Any? = null, // null
    @SerializedName("block_time_in_minutes")
    var blockTimeInMinutes: Int? = null, // 10
    @SerializedName("categories")
    var categories: List<String>? = null,
    @SerializedName("coingecko_rank")
    var coingeckoRank: Int? = null, // 6
    @SerializedName("coingecko_score")
    var coingeckoScore: Double? = null, // 85.878
    @SerializedName("community_score")
    var communityScore: Double? = null, // 73.457
    @SerializedName("developer_score")
    var developerScore: Double? = null, // 98.829
    @SerializedName("genesis_date")
    var genesisDate: String? = null, // 2009-01-03
    @SerializedName("public_interest_score")
    var publicInterestScore: Double? = null, // 100.0
    @SerializedName("public_interest_stats")
    var publicInterestStats: PublicInterestStats? = null,
    @SerializedName("status_updates")
    @Expose(deserialize = false)
    var statusUpdates: Any? = null
)

data class Image(
    @SerializedName("large")
    var large: String? = null, // https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579
    @SerializedName("small")
    var small: String? = null, // https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1547033579
    @SerializedName("thumb")
    var thumb: String? = null // https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png?1547033579
)

data class Description(
    @SerializedName("en")
    var en: String? = null // Bitcoin is the first successful internet money based on peer-to-peer technology; whereby no central bank or authority is involved in the transaction and production of the Bitcoin currency. It was created by an anonymous individual/group under the name, Satoshi Nakamoto. The source code is available publicly as an open source project, anybody can look at it and be part of the developmental process.Bitcoin is changing the way we see money as we speak. The idea was to produce a means of exchange, independent of any central authority, that could be transferred electronically in a secure, verifiable and immutable way. It is a decentralized peer-to-peer internet currency making mobile payment easy, very low transaction fees, protects your identity, and it works anywhere all the time with no central authority or banks.Bitcoin is design to have only 21 million BTC ever created, thus making it a deflationary currency. Bitcoin uses the <a href="https://www.coingecko.com/en?hashing_algorithm=SHA-256">SHA-256</a> hashing algorithm with an average transaction confirmation time of 10 minutes. Miners today are mining Bitcoin using ASIC chip dedicated to only mining Bitcoin, and the hash rate has shot up to peta hashes.Being the first successful online cryptography currency, Bitcoin has inspired other alternative currencies such as <a href="https://www.coingecko.com/en/coins/litecoin">Litecoin</a>, <a href="https://www.coingecko.com/en/coins/peercoin">Peercoin</a>, <a href="https://www.coingecko.com/en/coins/primecoin">Primecoin</a>, and so on.The cryptocurrency then took off with the innovation of the turing-complete smart contract by <a href="https://www.coingecko.com/en/coins/ethereum">Ethereum</a> which led to the development of other amazing projects such as <a href="https://www.coingecko.com/en/coins/eos">EOS</a>, <a href="https://www.coingecko.com/en/coins/tron">Tron</a>, and even crypto-collectibles such as <a href="https://www.coingecko.com/buzz/ethereum-still-king-dapps-cryptokitties-need-1-billion-on-eos">CryptoKitties</a>.
)

data class Links(
    @SerializedName("announcement_url")
    var announcementUrl: List<String>? = null,
    @SerializedName("bitcointalk_thread_identifier")
    var bitcointalkThreadIdentifier: Any? = null, // null
    @SerializedName("blockchain_site")
    var blockchainSite: List<String>? = null,
    @SerializedName("chat_url")
    var chatUrl: List<String>? = null,
    @SerializedName("facebook_username")
    var facebookUsername: String? = null, // bitcoins
    @SerializedName("homepage")
    var homepage: List<String>? = null,
    @SerializedName("official_forum_url")
    var officialForumUrl: List<String>? = null,
    @SerializedName("repos_url")
    var reposUrl: ReposUrl? = null,
    @SerializedName("subreddit_url")
    var subredditUrl: String? = null, // https://www.reddit.com/r/Bitcoin/
    @SerializedName("telegram_channel_identifier")
    var telegramChannelIdentifier: String? = null,
    @SerializedName("twitter_screen_name")
    var twitterScreenName: String? = null // btc
)

data class PublicInterestStats(
    @SerializedName("alexa_rank")
    var alexaRank: Int? = null, // 10026
    @SerializedName("bing_matches")
    var bingMatches: Any? = null // null
)

data class ReposUrl(
    @SerializedName("bitbucket")
    var bitbucket: List<Any>? = null,
    @SerializedName("github")
    var github: List<String>? = null
)