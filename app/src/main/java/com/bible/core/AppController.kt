package com.bible.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bible.BuildConfig
import com.bible.core.ConnectivityReceiver.networkCallback
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.facebook.stetho.Stetho

class AppController : MultiDexApplication() {
    companion object {


        @Volatile
        private var instance: AppController? = null
        private var mServiceClass: Any? = null
        private var previousTimeOut = 30000L
        private var previousUrl = ""
        private lateinit var connectivityManager: ConnectivityManager
        private var timeout = 30000L
        private lateinit var mManager: Any

        lateinit var versesDownload: BibleHomePageResponse

        lateinit var commentariesResponse: CommentariesResponse

        fun isversesDownloadInitialized() = ::versesDownload.isInitialized

        var versionId: String = "5"
        var versionName: String = "KJV"

        var flag: String = "0"

        var completeReadingDataSize:Int = 0

        var type: String = "B"

        var bookId: String = "1"
        var chapterId: String = "1"
        var titleId: String = "1"


        fun getInstance() = instance ?: synchronized(this) {
            instance
                ?: AppController().also {
                    instance = it
                }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build()
            )
        }

        registerConnectivityMonitor()
    }

    private fun registerConnectivityMonitor() {
        connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .build()
        unregisterConnectivityManager()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun unregisterConnectivityManager() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> serviceClass(
        mContext: Context,
        serviceClass: Class<T>?,
        apiUrl: String
    ): T {
        return if (mServiceClass == serviceClass && previousTimeOut == timeout && previousUrl == apiUrl) {
            mManager as T
        } else {
            previousTimeOut = timeout
            previousUrl = apiUrl
            mServiceClass = serviceClass
            build(mContext, apiUrl, timeout)
        }
    }

    private fun <T> build(
        mContext: Context,
        apiUrl: String,
        timeOut: Long
    ): T {
        mManager = NetworkManager.build(
            mContext,
            mServiceClass, apiUrl, timeOut
        ) as Any
        return mManager as T
    }


}