package com.himdeve.tmdb.interview.application.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by Himdeve on 10/1/2020.
 */

const val NO_INTERNET_CONNECTION = -1

object ConnectivityHelper {
    fun isOnline(context: Context): Boolean {
        return (getConnectionType(context) > -1)
    }

    private fun getConnectionType(context: Context): Int {
        var result = NO_INTERNET_CONNECTION
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = NetworkCapabilities.TRANSPORT_WIFI
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = NetworkCapabilities.TRANSPORT_CELLULAR
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = NetworkCapabilities.TRANSPORT_VPN
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            result = NetworkCapabilities.TRANSPORT_ETHERNET
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            result = NetworkCapabilities.TRANSPORT_WIFI
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            result = NetworkCapabilities.TRANSPORT_CELLULAR
                        }
                        ConnectivityManager.TYPE_VPN -> {
                            result = NetworkCapabilities.TRANSPORT_VPN
                        }
                        ConnectivityManager.TYPE_ETHERNET -> {
                            result = NetworkCapabilities.TRANSPORT_ETHERNET
                        }
                    }
                }
            }
        }
        return result
    }
}