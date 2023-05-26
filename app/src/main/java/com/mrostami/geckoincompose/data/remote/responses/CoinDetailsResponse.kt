//package com.mrostami.geckoin.model
//
//import com.google.gson.annotations.Expose
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class CoinDetailResponse(
//
//    @SerialName("id")
//    var id: String? = null, // bitcoin
//
//    @SerialName("symbol")
//    var symbol: String? = null, // btc
//
//    @SerialName("name")
//    var name: String? = null, // Bitcoin
//
//    @SerialName("market_cap_rank")
//    var marketCapRank: Int? = null, // 1
//
//    @SerialName("liquidity_score")
//    var liquidityScore: Double? = null, // 100.052
//
//    @SerialName("image")
//    var image: Image? = null,
//
//    @SerialName("description")
//    var description: Description? = null,
//
//    @SerialName("hashing_algorithm")
//    var hashingAlgorithm: String? = null, // SHA-256
//
//    @SerialName("country_origin")
//    var countryOrigin: String? = null,
//
//    @SerialName("last_updated")
//    var lastUpdated: String? = null, // 2020-07-25T08:08:09.170Z
//
//    @SerialName("sentiment_votes_down_percentage")
//    var sentimentVotesDownPercentage: Double? = null, // 27.8
//    @SerialName("sentiment_votes_up_percentage")
//    var sentimentVotesUpPercentage: Double? = null, // 72.2
//
//    @SerialName("links")
//    var links: Links? = null,
//
//    @SerialName("asset_platform_id")
//    @Expose(deserialize = false)
//    var assetPlatformId: Any? = null, // null
//    @SerialName("block_time_in_minutes")
//    var blockTimeInMinutes: Int? = null, // 10
//    @SerialName("categories")
//    var categories: List<String>? = null,
//    @SerialName("coingecko_rank")
//    var coingeckoRank: Int? = null, // 6
//    @SerialName("coingecko_score")
//    var coingeckoScore: Double? = null, // 85.878
//    @SerialName("community_score")
//    var communityScore: Double? = null, // 73.457
//    @SerialName("developer_score")
//    var developerScore: Double? = null, // 98.829
//    @SerialName("genesis_date")
//    var genesisDate: String? = null, // 2009-01-03
//    @SerialName("public_interest_score")
//    var publicInterestScore: Double? = null, // 100.0
//    @SerialName("public_interest_stats")
//    var publicInterestStats: PublicInterestStats? = null,
//    @SerialName("status_updates")
//    @Expose(deserialize = false)
//    var statusUpdates: Any? = null
//)
//
//@Serializable
//data class Image(
//    @SerialName("large")
//    var large: String? = null, // https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579
//    @SerialName("small")
//    var small: String? = null, // https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1547033579
//    @SerialName("thumb")
//    var thumb: String? = null // https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png?1547033579
//)
//
//@Serializable
//data class Description(
//    @SerialName("en")
//    var en: String? = null // Bitcoin is the first successful internet money based on peer-to-peer technology; whereby no central bank or authority is involved in the transaction and production of the Bitcoin currency. It was created by an anonymous individual/group under the name, Satoshi Nakamoto. The source code is available publicly as an open source project, anybody can look at it and be part of the developmental process.Bitcoin is changing the way we see money as we speak. The idea was to produce a means of exchange, independent of any central authority, that could be transferred electronically in a secure, verifiable and immutable way. It is a decentralized peer-to-peer internet currency making mobile payment easy, very low transaction fees, protects your identity, and it works anywhere all the time with no central authority or banks.Bitcoin is design to have only 21 million BTC ever created, thus making it a deflationary currency. Bitcoin uses the <a href="https://www.coingecko.com/en?hashing_algorithm=SHA-256">SHA-256</a> hashing algorithm with an average transaction confirmation time of 10 minutes. Miners today are mining Bitcoin using ASIC chip dedicated to only mining Bitcoin, and the hash rate has shot up to peta hashes.Being the first successful online cryptography currency, Bitcoin has inspired other alternative currencies such as <a href="https://www.coingecko.com/en/coins/litecoin">Litecoin</a>, <a href="https://www.coingecko.com/en/coins/peercoin">Peercoin</a>, <a href="https://www.coingecko.com/en/coins/primecoin">Primecoin</a>, and so on.The cryptocurrency then took off with the innovation of the turing-complete smart contract by <a href="https://www.coingecko.com/en/coins/ethereum">Ethereum</a> which led to the development of other amazing projects such as <a href="https://www.coingecko.com/en/coins/eos">EOS</a>, <a href="https://www.coingecko.com/en/coins/tron">Tron</a>, and even crypto-collectibles such as <a href="https://www.coingecko.com/buzz/ethereum-still-king-dapps-cryptokitties-need-1-billion-on-eos">CryptoKitties</a>.
//)
//@Serializable
//data class Links(
//    @SerialName("announcement_url")
//    var announcementUrl: List<String>? = null,
//    @SerialName("bitcointalk_thread_identifier")
//    var bitcointalkThreadIdentifier: Any? = null, // null
//    @SerialName("blockchain_site")
//    var blockchainSite: List<String>? = null,
//    @SerialName("chat_url")
//    var chatUrl: List<String>? = null,
//    @SerialName("facebook_username")
//    var facebookUsername: String? = null, // bitcoins
//    @SerialName("homepage")
//    var homepage: List<String>? = null,
//    @SerialName("official_forum_url")
//    var officialForumUrl: List<String>? = null,
//    @SerialName("repos_url")
//    var reposUrl: ReposUrl? = null,
//    @SerialName("subreddit_url")
//    var subredditUrl: String? = null, // https://www.reddit.com/r/Bitcoin/
//    @SerialName("telegram_channel_identifier")
//    var telegramChannelIdentifier: String? = null,
//    @SerialName("twitter_screen_name")
//    var twitterScreenName: String? = null // btc
//)
//@Serializable
//data class PublicInterestStats(
//    @SerialName("alexa_rank")
//    var alexaRank: Int? = null, // 10026
//    @SerialName("bing_matches")
//    var bingMatches: Any? = null // null
//)
//@Serializable
//data class ReposUrl(
//    @SerialName("bitbucket")
//    var bitbucket: List<Any>? = null,
//    @SerialName("github")
//    var github: List<String>? = null
//)