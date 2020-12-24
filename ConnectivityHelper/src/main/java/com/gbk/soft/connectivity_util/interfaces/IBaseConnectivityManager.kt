package com.gbk.soft.connectivity_util.interfaces

import androidx.lifecycle.LifecycleOwner

interface IBaseConnectivityManager {
    fun setConnectivityListener(
        lifecycleOwner: LifecycleOwner?,
        connectivityListener: IConnectivityListener?
    )
}