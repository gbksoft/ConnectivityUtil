package com.gbk.soft.connectivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gbk.soft.connectivity_util.ConnectivityManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityManager = ConnectivityManager(this)

        btnCheckConnectivity.setOnClickListener {
            textOnline.text = "Status : ${connectivityManager.isOnline}"
        }
    }
}