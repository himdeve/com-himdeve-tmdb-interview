package com.himdeve.tmdb.interview.application.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by Himdeve on 10/1/2020.
 */

// TODO: not finished! Don't use it yet

class ConnectivityStateMonitor(private val context: Context) : NetworkCallback() {
    val connectedLiveData: LiveData<Boolean>
        get() = _connectedMutableLiveData
    private val _connectedMutableLiveData = MutableLiveData<Boolean>()

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    fun enable() {
        // set current connectivity value (connected/disconnected)
        //  to not trigger the same value again in live data after initialization
        _connectedMutableLiveData.value = ConnectivityHelper.isOnline(context)

        // after connectivity live data initialization, register network callback
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun disable() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _connectedMutableLiveData.postValue(ConnectivityHelper.isOnline(context))
    }

    override fun onUnavailable() {
        super.onUnavailable()
        _connectedMutableLiveData.postValue(ConnectivityHelper.isOnline(context))
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _connectedMutableLiveData.postValue(ConnectivityHelper.isOnline(context))
    }
}