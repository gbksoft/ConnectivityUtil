package com.gbk.soft.connectivityutil.interfaces

interface IConnectivityListener {
    fun onNetworkStateChanged(isOnline: Boolean)
}