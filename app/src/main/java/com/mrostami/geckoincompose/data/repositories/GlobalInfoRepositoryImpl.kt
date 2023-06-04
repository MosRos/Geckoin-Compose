package com.mrostami.geckoincompose.data.repositories

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.TrendCoinsResponse
import com.mrostami.geckoincompose.data.local.LocalDataSource
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.model.MarketCapPercentageItem
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.utils.round
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class GlobalInfoRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : GlobalInfoRepository {

    val bitcoinId = "bitcoin"

    // Last Request Time variables
    val DEFAULT_RATE_LIMIT = 30*60*1000L
    var marketInfoLRT = 0L
    var trendCoinsLRT = 0L
    var btcPriceLRT = 0L
    var marketChartLRT = 0L

    override fun getMarketInfo(forceRefresh: Boolean): Flow<Result<GlobalMarketInfo>> {
        val result =
            object :
                RepositoryResourceAdapter<Any, GlobalMarketInfo, HttpResponse, CoinGeckoApiError>(
                    rateLimiter = DEFAULT_RATE_LIMIT,
                    forceRefresh = forceRefresh
                ) {
                override suspend fun getFromDatabase(): GlobalMarketInfo? {
                    return try {
                        localDataSource.getGlobalMarketInfo(id = 1)
                    } catch (e: Exception) {
                        Timber.e(e)
                        null
                    }
                }

                override suspend fun validateCache(cachedData: GlobalMarketInfo?): Boolean {
                    return cachedData != null
                }

                override fun shouldFetchFromApi(): Boolean {
                    val trigger: Long = System.currentTimeMillis() - marketInfoLRT
                    Timber.e("Triggered  Time is: $trigger  /n and LastRequeTime: $marketInfoLRT")
                    return forceRefresh || trigger > rateLimiter
                }

                override suspend fun getFromApi(): Either<CoinGeckoApiError, HttpResponse> {
                    marketInfoLRT = System.currentTimeMillis()
                    return remoteDataSource.getGlobalMarketInfo()
                }

                override suspend fun persistData(apiData: HttpResponse) {
                    saveGlobalInfo(apiData)
                }
            }

        return result.invoke(Any())
    }

    override fun getTrendingCoins(forceRefresh: Boolean): Flow<Result<List<TrendCoin>>> {
        val result = object :
            RepositoryResourceAdapter<Any, List<TrendCoin>, TrendCoinsResponse, CoinGeckoApiError>(
                rateLimiter = DEFAULT_RATE_LIMIT,
                forceRefresh = forceRefresh
            ) {
            override suspend fun getFromDatabase(): List<TrendCoin> {
                return localDataSource.getTrendCoins()
            }

            override suspend fun validateCache(cachedData: List<TrendCoin>?): Boolean {
                return !cachedData.isNullOrEmpty()
            }

            override fun shouldFetchFromApi(): Boolean {
                val trigger: Long = System.currentTimeMillis() - trendCoinsLRT
                return forceRefresh || trigger > rateLimiter
            }

            override suspend fun getFromApi(): Either<CoinGeckoApiError, TrendCoinsResponse> {
                trendCoinsLRT = System.currentTimeMillis()
                return remoteDataSource.getTrendingCoins()
            }

            override suspend fun persistData(apiData: TrendCoinsResponse) {
                val coins: List<TrendCoin>? = apiData.coinItems?.mapNotNull { it.item }
                if (!coins.isNullOrEmpty()) {
                    withContext(Dispatchers.IO) {
                        localDataSource.clearAllTrendCoins()
                        localDataSource.insertTrendCoins(coins)
                    }
                }
            }
        }
        return result.invoke(Any())
    }

//    override fun getBtcDailyPriceInfo(forceRefresh: Boolean): Flow<Result<BitcoinPriceInfo>> {
//        val result =
//            object :
//                RepositoryResourceAdapter<Any, BitcoinPriceInfo, BitcoinSimplePriceInfoResponse, CoinGeckoApiError>(
//                    rateLimiter = DEFAULT_RATE_LIMIT,
//                    forceRefresh = forceRefresh
//                ) {
//                override suspend fun getFromDatabase(): BitcoinPriceInfo? {
//                    return try {
//                        localDataSource.getBtcPriceInfo(id = bitcoinId)
//                    } catch (e: Exception) {
//                        Timber.e(e)
//                        null
//                    }
//                }
//
//                override suspend fun validateCache(cachedData: BitcoinPriceInfo?): Boolean {
//                    return cachedData != null
//                }
//
//                override fun shouldFetchFromApi(): Boolean {
//                    val trigger: Long = System.currentTimeMillis() - btcPriceLRT
//                    return forceRefresh || trigger > rateLimiter
//                }
//
//                override suspend fun getFromApi(): Either<CoinGeckoApiError, BitcoinSimplePriceInfoResponse> {
//                    btcPriceLRT = System.currentTimeMillis()
//                    return remoteDataSource.getBitcoinSimplePrice()
//                }
//
//                override suspend fun persistData(apiData: BitcoinSimplePriceInfoResponse) {
//                    withContext(Dispatchers.IO) {
//                        apiData.bitcoin?.let { btc ->
//                            localDataSource.putBtcPriceInfo(info = BitcoinPriceInfo(
//                                info = btc
//                            )
//                            )
//                        }
//                    }
//                }
//            }
//
//        return result.invoke(Any())
//    }
//
//    override fun getBtcMarketChartInfo(forceRefresh: Boolean): Flow<Result<List<PriceEntry>>> {
//        val result = object :
//                RepositoryResourceAdapter<Any, List<PriceEntry>, PriceChartResponse, CoinGeckoApiError>(
//                    rateLimiter = DEFAULT_RATE_LIMIT,
//                    forceRefresh = forceRefresh
//                ) {
//                override suspend fun getFromDatabase(): List<PriceEntry>? {
//                    return try {
//                        localDataSource.getPriceChartEntries(id = bitcoinId)
//                    } catch (e: Exception) {
//                        Timber.e(e)
//                        null
//                    }
//                }
//
//                override suspend fun validateCache(cachedData: List<PriceEntry>?): Boolean {
//                    return cachedData != null
//                }
//
//                override fun shouldFetchFromApi(): Boolean {
//                    val trigger: Long = System.currentTimeMillis() - marketChartLRT
//                    return forceRefresh || trigger > rateLimiter
//                }
//
//                override suspend fun getFromApi(): Either<PriceChartResponse, CoinGeckoApiError> {
//                    marketChartLRT = System.currentTimeMillis()
//                    return remoteDataSource.getMarketChartInfo(coinId = bitcoinId)
//                }
//
//                override suspend fun persistData(apiData: PriceChartResponse) {
//                    withContext(Dispatchers.IO) {
//                        localDataSource.deletePriceChartEntries(bitcoinId)
//                        val mapedData: List<PriceEntry> =
//                            apiData.prices?.filterNotNull()?.map { it ->
//                                PriceEntry(
//                                    coinId = bitcoinId,
//                                    timeStamp = it[0].toLong(),
//                                    price = it[1]
//                                )
//                            } ?: listOf()
//
//                        localDataSource.putPriceEntries(mapedData)
//                    }
//                }
//            }.invoke(Any())
//        return result
//    }

    private suspend fun saveGlobalInfo(httpResponse: HttpResponse) {
        if (httpResponse.status != HttpStatusCode.OK) return
        try {
            val responseData: String = httpResponse.bodyAsText()
            var responseJson = JSONObject(responseData)
            if (responseJson.has("data")) {
                responseJson = responseJson.getJSONObject("data")
            } else {
                return
            }
            val activeCCurrencies: Int = if (responseJson.has("active_cryptocurrencies")) {
                responseJson.getInt("active_cryptocurrencies")
            } else 0
            val upcomingIco: Int = if (responseJson.has("upcoming_icos")) {
                responseJson.getInt("upcoming_icos")
            } else 0
            val ongoingIco: Int = if (responseJson.has("ongoing_icos")) {
                responseJson.getInt("ongoing_icos")
            } else 0
            val endedIco: Int = if (responseJson.has("ended_icos")) {
                responseJson.getInt("ended_icos")
            } else 0
            val marketsNumber: Int = if (responseJson.has("markets")) {
                responseJson.getInt("markets")
            } else 0
            val marketCap24hChangePercentage: Double =
                if (responseJson.has("market_cap_change_percentage_24h_usd")) {
                    responseJson.getDouble("market_cap_change_percentage_24h_usd")
                } else 0.0
            val updateAt: Long = if (responseJson.has("updated_at")) {
                responseJson.getLong("updated_at")
            } else 0L

            val mCapPercentageItems: ArrayList<MarketCapPercentageItem> = arrayListOf()

            if (responseJson.has("market_cap_percentage")) {
                val mCapPercentageJSon: JSONObject =
                    responseJson.getJSONObject("market_cap_percentage")
                val keys: MutableIterator<String> = mCapPercentageJSon.keys()
                keys.forEach { k ->
                    val item = MarketCapPercentageItem(
                        coinId = k,
                        cap = mCapPercentageJSon.getDouble(k).round(decimals = 1)
                    )
                    mCapPercentageItems.add(item)
                }
            }

            val globalInfo = GlobalMarketInfo(
                activeCryptocurrencies = activeCCurrencies,
                endedIcos = endedIco,
                upcomingIcos = upcomingIco,
                ongoingIcos = ongoingIco,
                markets = marketsNumber,
                marketCapChangePercentage24hUsd = marketCap24hChangePercentage,
                marketCapPercentages = mCapPercentageItems.toList(),
                updatedAt = updateAt
            )

            withContext(Dispatchers.IO) {
                localDataSource.clearGlobalMarketInfo()
                localDataSource.putGlobalInfo(info = globalInfo)
            }

        } catch (e: Exception) {
            Timber.e(e)
        }


    }
}