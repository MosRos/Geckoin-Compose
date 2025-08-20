package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.CoinGeckoPingResponse
import com.mrostami.geckoin.data.remote.responses.PriceChartResponse
import com.mrostami.geckoin.data.remote.responses.TrendCoinsResponse
import com.mrostami.geckoincompose.model.BitcoinSimplePriceInfoResponse
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.model.RankedCoin
import io.ktor.client.statement.HttpResponse

interface CoinGeckoService {
    companion object {
        const val PING_ENDPOINT = "ping"
        const val ALL_COINS_ENDPOINT = "coins/list"
        const val GLOBAL_INFO_ENDPOINT = "global"
        const val TRENDING_COINS_ENDPOINT = "search/trending"
        const val MARKET_RANKS_ENDPOINT = "coins/markets"
        const val SIMPLE_PRICE_ENDPOINT = "simple/price"
        const val PRICE_CHART_ENDPOINT = "coins/{id}/market_chart"
        const val COIN_DETAIL = "coins/{id}"
        const val COIN_RANKING_ENDPOINT = "coins/"
    }

    suspend fun checkCoinGeckoConnection() : Either<CoinGeckoApiError, CoinGeckoPingResponse>

    suspend fun getAllCoins() : Either<CoinGeckoApiError, List<Coin>>

    suspend fun getGlobalMarketInfo() : Either<CoinGeckoApiError, HttpResponse>

    suspend fun getTrendingCoins() : Either<CoinGeckoApiError, TrendCoinsResponse>

    suspend fun getBitcoinSimplePrice() : Either<CoinGeckoApiError, BitcoinSimplePriceInfoResponse>

    suspend fun getPriceChartInfo(coinId: String) : Either<CoinGeckoApiError, PriceChartResponse>

    suspend fun getPagedMarketRanks(page: Int, perPage: Int) : Either<CoinGeckoApiError, List<RankedCoin>>
//
//    // Coin Detail info
//    @GET("coins/{id}?localization=false&tickers=false&market_data=false&community_data=false&developer_data=false&sparkline=true")
//    suspend fun getCoinDetailsInfo(
//        @Path("id") coinId: String
//    ) : NetworkResponse<CoinDetailResponse, CoinGeckoApiError>
}