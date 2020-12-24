package com.gbk.soft.connectivity_manager.interfaces

interface IConnectivityListener {
    fun onNetworkStateChanged(isOnline: Boolean)
}