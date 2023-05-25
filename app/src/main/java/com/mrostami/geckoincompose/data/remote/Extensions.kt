package com.mrostami.geckoincompose.data.remote

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException

suspend fun <R> HttpClient.requestAndCatch(
    request: suspend HttpClient.() -> Either<CoinGeckoApiError, R>,
    errorHandler: suspend ResponseException.() -> Either<CoinGeckoApiError, Any>
): Either<CoinGeckoApiError, R> = runCatching {
    request()
}.getOrElse {
    when (it) {
        is ResponseException -> Either.Left(CoinGeckoApiError()) //it.errorHandler()
        else -> Either.Left(CoinGeckoApiError())
    }
}

// Example call
//client.requestAndCatch(
//{ get<String>("/") },
//{
//    when (response.status) {
//        HttpStatusCode.BadRequest -> {} // Throw errors or transform to T
//        HttpStatusCode.Conflict -> {}
//        else -> throw this
//    }
//}
//)
