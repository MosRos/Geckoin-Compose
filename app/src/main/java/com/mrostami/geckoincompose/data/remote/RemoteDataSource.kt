package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.CoinGeckoPingResponse
import com.mrostami.geckoin.data.remote.responses.PriceChartResponse
import com.mrostami.geckoin.data.remote.responses.TrendCoinsResponse
import com.mrostami.geckoincompose.data.remote.responses.RankCoin
import com.mrostami.geckoincompose.model.BitcoinSimplePriceInfoResponse
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.model.RankedCoin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.appendEncodedPathSegments
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val coinGeckoKtorClient: CoinGeckoKtorClient
) : CoinGeckoService {
    private val BASE_URL: String = "https://api.coingecko.com/api/v3/"

    override suspend fun checkCoinGeckoConnection(): Either<CoinGeckoApiError, CoinGeckoPingResponse> {
        val url = BASE_URL + CoinGeckoService.PING_ENDPOINT
        return coinGeckoKtorClient.get(url)
    }

    override suspend fun getAllCoins(): Either<CoinGeckoApiError, List<Coin>> {
        val url = BASE_URL + CoinGeckoService.ALL_COINS_ENDPOINT
        return coinGeckoKtorClient.get(url)
    }

    override suspend fun getGlobalMarketInfo(): Either<CoinGeckoApiError, HttpResponse> {
        val url = BASE_URL + CoinGeckoService.GLOBAL_INFO_ENDPOINT
        return coinGeckoKtorClient.get(url)
    }

    override suspend fun getTrendingCoins(): Either<CoinGeckoApiError, TrendCoinsResponse> {
        val url = BASE_URL + CoinGeckoService.TRENDING_COINS_ENDPOINT
        return coinGeckoKtorClient.get(url)
    }

    override suspend fun getBitcoinSimplePrice(): Either<CoinGeckoApiError, BitcoinSimplePriceInfoResponse> {
        val url = BASE_URL + CoinGeckoService.SIMPLE_PRICE_ENDPOINT
        val requestBuilder = HttpRequestBuilder().apply {
            method = HttpMethod.Get
            parameter("ids", "bitcoin")
            parameter("vs_currencies", "usd")
            parameter("include_market_cap", true)
            parameter("include_24hr_vol", true)
            parameter("include_24hr_change", true)
            parameter("include_last_updated_at", true)
        }
        return coinGeckoKtorClient.get(url) {
            method = HttpMethod.Get
            parameter("ids", "bitcoin")
            parameter("vs_currencies", "usd")
            parameter("include_market_cap", true)
            parameter("include_24hr_vol", true)
            parameter("include_24hr_change", true)
            parameter("include_last_updated_at", true)
        }
    }

    override suspend fun getPriceChartInfo(coinId: String): Either<CoinGeckoApiError, PriceChartResponse> {
        val url = BASE_URL // + CoinGeckoService.PRICE_CHART_ENDPOINT
        return coinGeckoKtorClient.get(url) {
            method = HttpMethod.Get
            url {
                appendEncodedPathSegments("coins", coinId, "market_chart")
                parameter("vs_currency", "usd")
                parameter("days", 10)
                parameter("interval", "daily")
            }
        }
    }

    override suspend fun getPagedMarketRanks(
        page: Int,
        perPage: Int
    ): Either<CoinGeckoApiError, List<RankedCoin>> {
        val url = BASE_URL + CoinGeckoService.MARKET_RANKS_ENDPOINT
        return coinGeckoKtorClient.get<List<RankCoin>>(url) {
            method = HttpMethod.Get
            parameter("vs_currency", "usd")
            parameter("page", page)
            parameter("per_page", perPage)
        }.map { rankCoins ->
            rankCoins.map { RankCoin.toRankedEntity(it) }
        }
    }
}