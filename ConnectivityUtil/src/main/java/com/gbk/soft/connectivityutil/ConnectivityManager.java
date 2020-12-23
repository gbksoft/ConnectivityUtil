package com.gbk.soft.connectivityutil;

import android.content.Context;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import timber.log.Timber;

public class ConnectivityManager implements IConnectivityManager, IBaseConnectivityManager {
    private final Context context;
    private boolean isConnectionAvailable = false;
    private android.net.ConnectivityManager connectivityManager;
    private android.net.ConnectivityManager.NetworkCallback wifiNetworkCallback;
    private android.net.ConnectivityManager.NetworkCallback cellNetworkCallback;
    private boolean wifiConnected = false;
    private boolean cellConnected = false;
    private IConnectivityListener connectivityListener;
    private final MutableLiveData<Boolean> isOnlineLiveData = new MutableLiveData<>();

    public ConnectivityManager(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        connectivityManager = ((android.net.ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        Timber.w("ConnectivityManager: %b", connectivityManager != null);
        if (connectivityManager == null) {
            return;
        }
        initialNetworkCheck();
        onStateChanged();
        registerConnectivityListener();
    }

    private void initialNetworkCheck() {
        Network[] nets = connectivityManager.getAllNetworks();
        Timber.w("nets: %d", nets.length);
        for (int i = 0; i < nets.length; i++) {
            checkNetworkCell(nets[i], true);
            checkNetworkWifi(nets[i], true);
        }
        isConnectionAvailable = isConnectionAvailable && (nets.length > 0);
    }

    public void destroy() {
        unregisterCallback();
    }

    @Override
    public boolean isOnline() {
        initialNetworkCheck();
        return isConnectionAvailable;
    }

    private void registerConnectivityListener() {
        NetworkRequest wifiNetworkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
        wifiNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Timber.w("onAvailable: ");
                checkNetworkWifi(network, true);
                onStateChanged();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Timber.w("onLost: ");
                checkNetworkWifi(network, false);
                onStateChanged();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Timber.w("onUnavailable: ");
                isConnectionAvailable = false;
                onStateChanged();
            }
        };
        NetworkRequest cellNetworkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        cellNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Timber.w("onAvailable: ");
                checkNetworkCell(network, true);
                onStateChanged();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Timber.w("onLost: ");
                checkNetworkCell(network, false);
                onStateChanged();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Timber.w("onUnavailable: ");
                isConnectionAvailable = false;
                onStateChanged();
            }
        };
        connectivityManager.registerNetworkCallback(wifiNetworkRequest, wifiNetworkCallback);
        connectivityManager.registerNetworkCallback(cellNetworkRequest, cellNetworkCallback);
    }

    private void checkNetworkWifi(Network network, boolean networkStatus) {
        wifiConnected = false;
        if (network != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                wifiConnected = networkStatus;
            }
        }
        isConnectionAvailable = wifiConnected || cellConnected;
        Timber.w("checkNetwork: wifi: %b,  cell: %b", wifiConnected, cellConnected);
    }

    private void checkNetworkCell(Network network, boolean networkStatus) {
        cellConnected = false;
        if (network != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                cellConnected = networkStatus;
            }
        }
        isConnectionAvailable = wifiConnected || cellConnected;
        Timber.w("checkNetwork: wifi: %b,  cell: %b", wifiConnected, cellConnected);
    }

    private void onStateChanged() {
        Timber.w("onStateChanged: %b", isConnectionAvailable);
        isOnlineLiveData.postValue(isConnectionAvailable);
    }

    private void unregisterCallback() {
        if (connectivityManager != null) {
            android.net.ConnectivityManager.NetworkCallback  localWifi = wifiNetworkCallback;
            if(localWifi != null) {
                try {
                    connectivityManager.unregisterNetworkCallback(localWifi);
                } catch ( IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            android.net.ConnectivityManager.NetworkCallback  localCell = cellNetworkCallback;
            if(localCell != null) {
                try {
                    connectivityManager.unregisterNetworkCallback(localCell);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setConnectivityListener(LifecycleOwner lifecycleOwner, IConnectivityListener connectivityListener) {
        this.connectivityListener = connectivityListener;
        addListener(lifecycleOwner, connectivityListener);
    }

    private void addListener(LifecycleOwner lifecycleOwner, IConnectivityListener connectivityListener) {
        isOnlineLiveData.observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                connectivityListener.onNetworkStateChanged(aBoolean);
            }
        });
    }
}