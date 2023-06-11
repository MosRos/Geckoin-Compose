package com.mrostami.geckoincompose.di

import android.content.Context
import androidx.annotation.NonNull
import com.mrostami.geckoincompose.data.remote.KtorHttpLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val BASE_URL: String = "https://api.coingecko.com/api/v3/"

    @Singleton
    @Provides
    fun okHttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, 10 * 1000 * 1000)
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
//            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }


    @Singleton
    @Provides
    fun provideHttpClient(@NonNull loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideKtorLogger() : Logger = KtorHttpLogger()

    @Singleton
    @Provides
    @Named("ktor-android")
    fun provideKtorClient(ktorLogger: Logger): HttpClient {
        return HttpClient(Android) {
            defaultRequest {
                url.takeFrom(URLBuilder().takeFrom(BASE_URL).apply {
                    encodedPath += url.encodedPath
                })
//                header("key", "value")
//                header("key1", "value1")

            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 15_000
            }
            install(Logging) {
                logger = ktorLogger
                level = LogLevel.BODY
            }
//            HttpResponseValidator {  }
            KtorResponseInterceptor()
            engine {
                connectTimeout = 30_000
            }
        }
    }

    @Singleton
    @Provides
    @Named("ktor-okhttp")
    fun proviceKtorOkhttp(
        okHttpClient: OkHttpClient,
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                    connectTimeout(20, TimeUnit.SECONDS)
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(20, TimeUnit.SECONDS)
                    cache(cache)
                    addInterceptor(loggingInterceptor)
                    retryOnConnectionFailure(true)
                    preconfigured = okHttpClient
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
//                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )

            }
        }
    }
}