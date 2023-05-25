package com.mrostami.geckoincompose.data.remote

import io.ktor.client.plugins.logging.Logger
import timber.log.Timber

class KtorHttpLogger : Logger {
    override fun log(message: String) {
//        if (BuildConfig.DEBUG) {
//        }
        Timber.i(message)
    }
}