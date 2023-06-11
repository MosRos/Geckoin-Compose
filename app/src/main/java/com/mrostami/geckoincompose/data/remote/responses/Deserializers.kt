package com.mrostami.geckoincompose.data.remote.responses

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mrostami.geckoin.data.remote.responses.PriceChartResponse
import com.mrostami.geckoincompose.model.RankedCoin
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

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