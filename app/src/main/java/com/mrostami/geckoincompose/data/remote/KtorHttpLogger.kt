package com.mrostami.geckoincompose.data.remote

import com.mrostami.geckoincompose.BuildConfig
import io.ktor.client.plugins.logging.Logger
import timber.log.Timber

class KtorHttpLogger : Logger {
    override fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Timber.i(message)
        }
    }
}