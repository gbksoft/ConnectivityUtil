package com.gbk.soft.connectivityutil

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
   // private lateinit var connectivityManager: ConnectivityManager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //connectivityManager = ConnectivityManager2(this)

        btnCheckConnectivity.setOnClickListener {
            //textOnline.text = "Status : ${connectivityManager.isOnline}"
        }
    }
}