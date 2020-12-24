package com.gbk.soft.connectivityutil.interfaces

import androidx.lifecycle.LifecycleOwner

interface IBaseConnectivityManager {
    fun setConnectivityListener(
        lifecycleOwner: LifecycleOwner?,
        connectivityListener: IConnectivityListener?
    )
}