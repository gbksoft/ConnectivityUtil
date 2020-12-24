package com.gbk.soft.connectivity_util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gbk.soft.connectivity_util.interfaces.IBaseConnectivityManager
import com.gbk.soft.connectivity_util.interfaces.IConnectivityListener
import com.gbk.soft.connectivity_util.interfaces.IConnectivityManager
import timber.log.Timber

class Connectivity(private val context: Context) : IConnectivityManager,
    IBaseConnectivityManager {
    private var isConnectionAvailable = false
    private var connectivityManager: ConnectivityManager? = null
    private var wifiNetworkCallback: NetworkCallback? = null
    private var cellNetworkCallback: NetworkCallback? = null
    private var wifiConnected = false
    private var cellConnected = false
    private var connectivityListener: IConnectivityListener? = null
    private val isOnlineLiveData = MutableLiveData<Boolean>()

    override val isOnline: Boolean
        get() {
            initialNetworkCheck()
            return isConnectionAvailable
        }

    init {
        init()
    }

    private fun init() {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Timber.w("ConnectivityManager: %b", connectivityManager != null)
        if (connectivityManager == null) {
            return
        }
        initialNetworkCheck()
        onStateChanged()
        registerConnectivityListener()
    }

    private fun initialNetworkCheck() {
        val nets = connectivityManager!!.allNetworks
        Timber.w("nets: %d", nets.size)
        for (i in nets.indices) {
            checkNetworkCell(nets[i], true)
            checkNetworkWifi(nets[i], true)
        }
        isConnectionAvailable = isConnectionAvailable && nets.isNotEmpty()
    }

    private fun registerConnectivityListener() {
        val wifiNetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        wifiNetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.w("onAvailable: ")
                checkNetworkWifi(network, true)
                onStateChanged()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.w("onLost: ")
                checkNetworkWifi(network, false)
                onStateChanged()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Timber.w("onUnavailable: ")
                isConnectionAvailable = false
                onStateChanged()
            }
        }
        val cellNetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        cellNetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.w("onAvailable: ")
                checkNetworkCell(network, true)
                onStateChanged()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.w("onLost: ")
                checkNetworkCell(network, false)
                onStateChanged()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Timber.w("onUnavailable: ")
                isConnectionAvailable = false
                onStateChanged()
            }
        }
        connectivityManager?.registerNetworkCallback(
            wifiNetworkRequest,
            wifiNetworkCallback as NetworkCallback
        )
        connectivityManager?.registerNetworkCallback(
            cellNetworkRequest,
            cellNetworkCallback as NetworkCallback
        )
    }

    private fun checkNetworkWifi(network: Network?, networkStatus: Boolean) {
        wifiConnected = false
        if (network != null) {
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                wifiConnected = networkStatus
            }
        }
        isConnectionAvailable = wifiConnected || cellConnected
        Timber.w("checkNetwork: wifi: %b,  cell: %b", wifiConnected, cellConnected)
    }

    private fun checkNetworkCell(network: Network?, networkStatus: Boolean) {
        cellConnected = false
        if (network != null) {
            val capabilities = connectivityManager?.getNetworkCapabilities(network)
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                cellConnected = networkStatus
            }
        }
        isConnectionAvailable = wifiConnected || cellConnected
        Timber.w("checkNetwork: wifi: %b,  cell: %b", wifiConnected, cellConnected)
    }

    private fun onStateChanged() {
        Timber.w("onStateChanged: %b", isConnectionAvailable)
        isOnlineLiveData.postValue(isConnectionAvailable)
    }

    private fun unregisterCallback() {
        if (connectivityManager != null) {
            val localWifi = wifiNetworkCallback
            if (localWifi != null) {
                try {
                    connectivityManager?.unregisterNetworkCallback(localWifi)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }
            val localCell = cellNetworkCallback
            if (localCell != null) {
                try {
                    connectivityManager?.unregisterNetworkCallback(localCell)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun setConnectivityListener(
        lifecycleOwner: LifecycleOwner?,
        connectivityListener: IConnectivityListener?
    ) {
        this.connectivityListener = connectivityListener
        addListener(lifecycleOwner, connectivityListener)
    }

    private fun addListener(
        lifecycleOwner: LifecycleOwner?,
        connectivityListener: IConnectivityListener?
    ) {
        isOnlineLiveData.observe(lifecycleOwner!!, { aBoolean ->
            connectivityListener?.onNetworkStateChanged(
                aBoolean!!
            )
        })
    }

    private fun destroy() {
        unregisterCallback()
    }


}