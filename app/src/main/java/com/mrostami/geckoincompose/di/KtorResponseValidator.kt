package com.mrostami.geckoincompose.di

import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

fun HttpClientConfig<*>.KtorResponseInterceptor() {
    val block:  (HttpCallValidator.Config) -> Unit = {
        it.handleResponseExceptionWithRequest { cause, request ->
            when(cause) {
                is ClientRequestException -> { // ktor: 400
                    val errorResponse = cause.response
                    val message =
                        Json.decodeFromString<CoinGeckoApiError>(cause.response.body()).error
                    when (errorResponse.status) {
                        HttpStatusCode.Unauthorized -> {
//                            throw ApiErrorType.UnAuthorized(message)
                        }
                        HttpStatusCode.NotFound -> {
//                            throw ApiErrorType.NotFound(message)
                        }
                        HttpStatusCode.Forbidden -> {
//                            throw ApiErrorType.Forbidden(message)
                        }
                        HttpStatusCode.UnprocessableEntity -> {
//                            throw ApiErrorType.UnprocessableEntity(message)
                        }
                        else -> {
//                            throw ApiErrorType.Unknown(message)
                        }
                    }
                }
                else -> {
                    Timber.e(cause)
                }
            }
        }
    }
}