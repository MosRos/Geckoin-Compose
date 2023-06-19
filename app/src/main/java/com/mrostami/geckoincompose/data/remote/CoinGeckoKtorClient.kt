package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoin.data.remote.responses.toCustomExceptions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class CoinGeckoKtorClient @Inject constructor(
    @Named("ktor-okhttp") val httpClient: HttpClient
) {
    suspend inline fun <reified T: Any> get(
        url: String,
        requestBuilder: HttpRequestBuilder.() -> Unit = {}
    ) : Either<CoinGeckoApiError, T> = try {
        val result = httpClient
            .get(url, requestBuilder)
            .body<T>()
        Either.Right(result)
    } catch (e: Exception) {
        Timber.e(e)
        Either.Left(e.toCustomExceptions())
    }

    suspend inline fun <reified T> post(
        url: String,
        requestBuilder: HttpRequestBuilder.() -> Unit = {}
    ) : Either<CoinGeckoApiError, T> = try {
        val result = httpClient
            .post(url, requestBuilder)
            .body<T>()
        Either.Right(result)
    } catch (e: Exception) {
        Timber.e(e)
        Either.Left(e.toCustomExceptions())
    }

    suspend inline fun <reified T> put(
        url: String,
        requestBuilder: HttpRequestBuilder.() -> Unit = {}
    ) : Either<CoinGeckoApiError, T> = try {
        val result = httpClient
            .put(url, requestBuilder)
            .body<T>()
        Either.Right(result)
    } catch (e: Exception) {
        Timber.e(e)
        Either.Left(e.toCustomExceptions())
    }
}