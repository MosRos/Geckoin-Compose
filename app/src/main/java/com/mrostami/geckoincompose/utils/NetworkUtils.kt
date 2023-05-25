/*
 * *
 *  * Created by Moslem Rostami on 6/19/20 12:19 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/19/20 12:19 PM
 *
 */

package com.mrostami.geckoincompose.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.mrostami.geckoincompose.GeckoinApp

object NetworkUtils {

    fun isConnected(): Boolean {

        var mIsConnected = false
        val mConnectivityMgr: ConnectivityManager? = GeckoinApp.getInstance().getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Checking internet connectivity
            var activeNetwork: NetworkInfo? = null
            if (mConnectivityMgr != null) {
                activeNetwork = mConnectivityMgr?.activeNetworkInfo // Deprecated in API 29
            }
            mIsConnected = activeNetwork != null
        } else {
            val allNetworks = mConnectivityMgr?.allNetworks // added in API 21 (Lollipop)
            if (allNetworks != null) {
                for (network in allNetworks) {
                    val networkCapabilities = mConnectivityMgr?.getNetworkCapabilities(network)
                    if (networkCapabilities != null) {
                        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                        )
                            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                            ) mIsConnected = true
                    }
                }
            }
        }
        return mIsConnected
    }
}