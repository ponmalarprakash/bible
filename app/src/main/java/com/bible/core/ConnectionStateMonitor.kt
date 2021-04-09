package com.bible.core

import android.annotation.TargetApi
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.*
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.LiveData
import com.bible.R

class ConnectionStateMonitor(val context: Context) : LiveData<Boolean>() {

    private var mNetworkDialog: Dialog? = null
    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityMarshmallowManagerCallback())
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> marshmallowNetworkAvailableRequest()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
            else -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    context.registerReceiver(
                        networkReceiver,
                        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    )
                }
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityLollipopManagerCallback()
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun marshmallowNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityMarshmallowManagerCallback()
        )
    }

    private fun getConnectivityLollipopManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("Accessing wrong API version")
        }
    }

    private fun getConnectivityMarshmallowManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkCapabilities?.let { capabilities ->
                        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED
                            )
                        ) {
                            postValue(true)
                        }
                    }
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("Accessing wrong API version")
        }

    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)

    }

    fun noNetworkConnection() {
        try {

            if(mNetworkDialog !=null)
                if (mNetworkDialog!!.isShowing())
                    mNetworkDialog!!.dismiss()

            val view = View.inflate(
                context,
                R.layout.internet_screen,
                null
            )
            mNetworkDialog = Dialog(
                context,
                R.style.Theme_Transparent
            )
            mNetworkDialog!!.setContentView(view)
            mNetworkDialog!!.setCancelable(false)
            mNetworkDialog!!.setCanceledOnTouchOutside(false)
            val window: Window =
               mNetworkDialog!!.getWindow()!!
            window.setGravity(Gravity.TOP)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }


            mNetworkDialog!!.show()
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
    }

    fun  hideNetworkView()
    {
        if(mNetworkDialog !=null)
            if (mNetworkDialog!!.isShowing())
                mNetworkDialog!!.dismiss()
    }

}
