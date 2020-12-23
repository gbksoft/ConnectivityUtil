package com.gbk.soft.connectivityutil

import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*  val cm: com.gbk.soft.connectivityutil.ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as com.gbk.soft.connectivityutil.ConnectivityManager
        val activeNetwork: NetworkInfo = cm.getActiveNetworkInfo()
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting*/
    }
}