package com.mrostami.geckoin.data.remote.responses

import com.google.gson.annotations.SerializedName
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class CoinGeckoApiError(
    @SerialName("error")
    var error: String? = "invalid"
)

fun Exception.toCustomExceptions() = when (this) {
    is ServerResponseException -> CoinGeckoApiError(error = this.message)
    is ClientRequestException ->
        when (this.response.status.value) {
            400 -> CoinGeckoApiError(error = "bad request: ${this.message}")
            401 -> CoinGeckoApiError(error = "Unauthorized! ${this.message}")
            403 -> CoinGeckoApiError(error = "Forbidden: ${this.message}")
            404 -> CoinGeckoApiError(error = "404 Not Found: ${this.message}")
            else ->CoinGeckoApiError(error = this.message)
        }
    is RedirectResponseException -> CoinGeckoApiError(error = this.message)
    else -> CoinGeckoApiError(error = "An error occurred!")
}