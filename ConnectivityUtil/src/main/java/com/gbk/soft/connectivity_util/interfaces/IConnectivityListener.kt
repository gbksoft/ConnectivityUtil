package com.gbk.soft.connectivity_util.interfaces

interface IConnectivityListener {
    fun onNetworkStateChanged(isOnline: Boolean)
}