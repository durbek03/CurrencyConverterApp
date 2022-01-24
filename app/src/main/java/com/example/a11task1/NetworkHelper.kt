package com.example.a11task1

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

class NetworkHelper(val context: Context) {

    fun isNetworkConnected():Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            result = checkNetwork(connectivityManager, activeNetwork)
        } else {
            val allNetworks = connectivityManager.allNetworks
            for (network in allNetworks) {
                result = checkNetwork(connectivityManager, network)
            }
        }
        return result
    }

    fun checkNetwork(connectivityManager: ConnectivityManager, network: Network): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}