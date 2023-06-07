package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.CoinGeckoPingResponse
import com.mrostami.geckoin.data.remote.responses.PriceChartResponse
import com.mrostami.geckoin.data.remote.responses.TrendCoinsResponse
import com.mrostami.geckoincompose.data.remote.responses.RankCoin
import com.mrostami.geckoincompose.model.BitcoinSimplePriceInfoResponse
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.model.RankedCoin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.encodeURLPath
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class RemoteDataSource @Inject constructor(
    @Named("ktor-okhttp") val httpClient: HttpClient
) : CoinGeckoService {

    private val BASE_URL: String = "https://api.coingecko.com/api/v3/"

    //    private val coinGeckoApiService by lazy {
//        retrofit.create(CoinGeckoService::class.java)
//    }
    override suspend fun checkCoinGeckoConnection(): Either<CoinGeckoApiError, CoinGeckoPingResponse> {
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + "ping") {
                        method = HttpMethod.Get
                    }
                    val result = Json.decodeFromString<CoinGeckoPingResponse>(response.body())
                    Either.Right(result)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getAllCoins(): Either<CoinGeckoApiError, List<Coin>> {
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + "coins/list") {
                        method = HttpMethod.Get
                    }
                    val result = Json.decodeFromString<List<Coin>>(response.body())
                    Either.Right(result)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getGlobalMarketInfo(): Either<CoinGeckoApiError, HttpResponse> {
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + "global") {
                        method = HttpMethod.Get
                    }
                    Either.Right(response)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getTrendingCoins(): Either<CoinGeckoApiError, TrendCoinsResponse> {
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + "search/trending") {
                        method = HttpMethod.Get
                    }
                    val result = Json.decodeFromString<TrendCoinsResponse>(response.body())
                    Either.Right(result)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getBitcoinSimplePrice(): Either<CoinGeckoApiError, BitcoinSimplePriceInfoResponse> {
        val endPoint = "simple/price"
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + endPoint) {
                        method = HttpMethod.Get
                        parameter("ids", "bitcoin")
                        parameter("vs_currencies", "usd")
                        parameter("include_market_cap", true)
                        parameter("include_24hr_vol", true)
                        parameter("include_24hr_change", true)
                        parameter("include_last_updated_at", true)
                    }
                    val result = Json.decodeFromString<BitcoinSimplePriceInfoResponse>(response.body())
                    Either.Right(result)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getPriceChartInfo(coinId: String): Either<CoinGeckoApiError, PriceChartResponse> {
        val encodedId = coinId.encodeURLPath()
        val endPoint = "coins/{$encodedId}/market_chart"
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.get(BASE_URL) {
                        url {
                            appendEncodedPathSegments("coins", coinId, "market_chart")
                            parameter("vs_currency", "usd")
                            parameter("days", 10)
                            parameter("interval", "daily")
                        }
                    }
                    val result = deserializePriceChartResponse(httpResponse = response) // Json.decodeFromString<PriceChartResponse>(response.body())
                    Either.Right(result)
                } catch (e : Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }

    override suspend fun getPagedMarketRanks(page: Int, perPage: Int): Either<CoinGeckoApiError, List<RankedCoin>> {
        val endpoint = "coins/markets"
        return httpClient.requestAndCatch(
            request = {
                try {
                    val response = httpClient.request(BASE_URL + endpoint) {
                        method = HttpMethod.Get
                        parameter("vs_currency", "usd")
                        parameter("page", page)
                        parameter("per_page", perPage)
                    }
//                    val result = Json.decodeFromString<List<RankCoin>>(response.body())
//                    val mapped = result.map { RankCoin.toRankedEntity(it) }
                    val result: List<RankedCoin> = deserializeRankedCoins(response)
                    Either.Right(result)
                } catch (e: Exception) {
                    Timber.e(e)
                    Either.Left(CoinGeckoApiError(error = e.message))
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError(error = this.message))
            }
        )
    }
}

private suspend fun deserializePriceChartResponse(httpResponse: HttpResponse) : PriceChartResponse {
    val priceEntries: MutableList<List<Double>> = mutableListOf()
    val priceChartResponse = PriceChartResponse(prices = priceEntries)

    if (httpResponse.status != HttpStatusCode.OK) return priceChartResponse
    try {
        val responseData: String = httpResponse.bodyAsText()
        val responseJson = JSONObject(responseData)
        var pricesJson = JSONArray()
        if (responseJson.has("prices")) {
            pricesJson = responseJson.getJSONArray("prices")
        } else {
            return priceChartResponse
        }
        for (i in 0 until  pricesJson.length()) {
            val entryJsonArray = pricesJson.getJSONArray(i)
            priceEntries.add(
                listOf(
                    entryJsonArray.getLong(0).toDouble(),
                    entryJsonArray.getDouble(1)
                )
            )
        }
        return PriceChartResponse(prices = priceEntries)
    } catch (e: Exception) {
        Timber.e(e)
        return priceChartResponse
    }
}

private suspend fun deserializeRankedCoins(httpResponse: HttpResponse) : List<RankedCoin> {
    val gson = Gson().newBuilder().create()
    val coins: MutableList<RankedCoin> = mutableListOf()
    if (httpResponse.status != HttpStatusCode.OK) return emptyList()
    try {
        val responseData: String = httpResponse.bodyAsText()
        val coinsJSONArray = JSONArray(responseData)
        if (coinsJSONArray.length() == 0) return emptyList()
        Timber.e("Deserializing", coinsJSONArray.toString())
        val type = TypeToken.getParameterized(List::class.java, RankedCoin::class.java).type
        for (i in 0 until coinsJSONArray.length()) {
        }
        val coinList = gson.fromJson<List<RankedCoin>>(responseData, type)
        coins.addAll(coinList)
        return coins.toList()
    } catch (e:Exception) {
        Timber.e(e.message)
        return emptyList() // listOf(RankedCoin(id = "bitcoin", name = "bitcoin"), RankedCoin(id = "ethereum", name = "ethereum"), RankedCoin(id = "shitcoin", name = "shitcoin"))
    }
}