package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.CoinGeckoPingResponse
import com.mrostami.geckoin.data.remote.responses.TrendCoinsResponse
import com.mrostami.geckoincompose.model.Coin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
                    Either.Left(CoinGeckoApiError())
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError())
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
                    Either.Left(CoinGeckoApiError())
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError())
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
                    Either.Left(CoinGeckoApiError())
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError())
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
                    Either.Left(CoinGeckoApiError())
                }
            },
            errorHandler = {
                Either.Left(CoinGeckoApiError())
            }
        )
    }

}