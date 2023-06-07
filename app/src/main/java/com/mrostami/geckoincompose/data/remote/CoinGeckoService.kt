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

    // Ping for api connection checking
//    @GET("ping")
    suspend fun checkCoinGeckoConnection() : Either<CoinGeckoApiError, CoinGeckoPingResponse>

    // All coins list
//    @GET("coins/list")
    suspend fun getAllCoins() : Either<CoinGeckoApiError, List<Coin>>

    // Global market info
//    @GET("global")
    suspend fun getGlobalMarketInfo() : Either<CoinGeckoApiError, HttpResponse>

    // Top Search and Trend coins
//    @GET("search/trending")
    suspend fun getTrendingCoins() : Either<CoinGeckoApiError, TrendCoinsResponse>

    suspend fun getBitcoinSimplePrice() : Either<CoinGeckoApiError, BitcoinSimplePriceInfoResponse>
    suspend fun getPriceChartInfo(coinId: String) : Either<CoinGeckoApiError, PriceChartResponse>

     //Market Ranks
//    @GET("coins/markets")
    suspend fun getPagedMarketRanks(page: Int, perPage: Int) : Either<CoinGeckoApiError, List<RankedCoin>>

    // Price Info for a coin
//    @GET("simple/price")
//    suspend fun getBitcoinSimplePrice(
//        @Query("ids") id: String = "bitcoin",
//        @Query("vs_currencies") vsCurrency: String = "usd",
//        @Query("include_market_cap") includeMarketCap: Boolean = true,
//        @Query("include_24hr_vol") include24HrVolume: Boolean = true,
//        @Query("include_24hr_change") include24HrChange: Boolean = true,
//        @Query("include_last_updated_at") includeLastUpdateTime: Boolean = true
//    ) : NetworkResponse<BitcoinSimplePriceInfoResponse, CoinGeckoApiError>
//
//    // Price Info for a coin
//    @GET("simple/price")
//    suspend fun getSimplePrice(
//        @Query("ids") id: String = "bitcoin",
//        @Query("vs_currencies") vsCurrency: String = "usd",
//        @Query("include_market_cap") includeMarketCap: Boolean = true,
//        @Query("include_24hr_vol") include24HrVolume: Boolean = true,
//        @Query("include_24hr_change") include24HrChange: Boolean = true,
//        @Query("include_last_updated_at") includeLastUpdateTime: Boolean = true
//    ) : NetworkResponse<ResponseBody, CoinGeckoApiError>
//
//    // Price chart Info
//    @GET("coins/{id}/market_chart")
//    suspend fun getMarketChartInfo(
//        @Path("id") coinId: String,
//        @Query("vs_currency") vsCurrency: String = "usd",
//        @Query("days") days: Int = 10,
//        @Query("interval") interval: String = "daily"
//    ) : NetworkResponse<PriceChartResponse, CoinGeckoApiError>
//
//    // Market Ranks
//    @GET("coins/markets")
//    suspend fun getPagedMarketRanks(
//        @Query("vs_currency") vs_currency: String = "usd",
//        @Query("page") page: Int,
//        @Query("per_page") per_page: Int
//    ) : List<RankedCoin>
//
//    // Coin Detail info
//    @GET("coins/{id}?localization=false&tickers=false&market_data=false&community_data=false&developer_data=false&sparkline=true")
//    suspend fun getCoinDetailsInfo(
//        @Path("id") coinId: String
//    ) : NetworkResponse<CoinDetailResponse, CoinGeckoApiError>
}