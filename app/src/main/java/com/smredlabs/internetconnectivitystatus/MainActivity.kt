package com.smredlabs.internetconnectivitystatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use ConnectivityManager to check whether you are actually connected to the Internet,
        // and if so, what type of connection is in place.
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var networkTransport = ""
        var sdkVersion = ""
        var available = "No"


        if(Build.VERSION.SDK_INT > 22 ){
            // For devices with API >= 23
            sdkVersion = "Version > 22"
            val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            // NET_CAPABILITY_VALIDATED - Indicates that connectivity on this network was successfully validated.
            // NET_CAPABILITY_INTERNET - Indicates that this network should be able to reach the internet.
            if(networkCapabilities!=null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
                available = "Has"
                if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    networkTransport = "WIFI"
                }
                else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    networkTransport = "Cellular"
                }
            }
        }
        else{
            // For devices with API < 23
            sdkVersion = "Version < 22"
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            val isWiFi: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_WIFI
            val isMob: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_MOBILE
            if(isConnected) {
                available = "Has"
                if (isWiFi) {
                    networkTransport = "WIFI"
                } else if (isMob) {
                    networkTransport = "Cellular"
                }
            }
        }

        tvHello.text = "$available Connectivity with $networkTransport - $sdkVersion"

    }
}
