package com.bible.core

import android.net.ConnectivityManager
import android.net.Network


object ConnectivityReceiver {
    var isOnline: Boolean = false
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            isOnline = false
        }

        override fun onUnavailable() {
            isOnline = false
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            isOnline = false
        }

        override fun onAvailable(network: Network) {
            isOnline = true
        }
    }
}