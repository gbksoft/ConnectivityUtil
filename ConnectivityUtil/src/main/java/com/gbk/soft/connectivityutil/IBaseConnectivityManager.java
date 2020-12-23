package com.gbk.soft.connectivityutil;

import androidx.lifecycle.LifecycleOwner;
public interface IBaseConnectivityManager {
    void setConnectivityListener(LifecycleOwner lifecycleOwner,IConnectivityListener connectivityListener);
}