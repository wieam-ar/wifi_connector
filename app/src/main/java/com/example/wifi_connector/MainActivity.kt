package com.example.wifi_connector

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var wifiIcon: ImageView
    private lateinit var statusText: TextView
    private lateinit var openSettingsBtn: Button

    // network callback to watch network events
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            runOnUiThread {
                val isWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                updateUi(isWifi)
            }
        }

        override fun onLost(network: Network) {
            runOnUiThread { updateUi(false) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiIcon = findViewById(R.id.wifiIcon)
        statusText = findViewById(R.id.statusText)
        openSettingsBtn = findViewById(R.id.openSettingsBtn)

        connectivityManager = getSystemService(ConnectivityManager::class.java)

        // Build a request for networks we care about (any; we'll check transport type)
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        // Button opens Wi-Fi settings screen so user can enable/connect
        openSettingsBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        // initial UI state check
        checkInitialNetwork()
    }

    private fun checkInitialNetwork() {
        val active = connectivityManager.activeNetwork
        if (active != null) {
            val caps = connectivityManager.getNetworkCapabilities(active)
            val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
            updateUi(isWifi)
        } else {
            updateUi(false)
        }
    }

    private fun updateUi(isWifiConnected: Boolean) {
        if (isWifiConnected) {
            wifiIcon.setImageResource(R.drawable.images1)       // normal wifi icon
            statusText.text = "Connected to Wi-Fi"
        } else {
            wifiIcon.setImageResource(R.drawable.wifi_off)   // wifi with X icon
            statusText.text = "Not connected to Wi-Fi"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
        }
    }
}
