package com.bible.core

import android.content.Context
import com.bible.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager {

    companion object {
        private lateinit var baseUrl: String
        //        private var serviceClass: Any? = null
        private lateinit var builder: Retrofit

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        fun baseUrl(baseUrl: String) = apply { Companion.baseUrl = baseUrl }

        /*fun <T> serviceClass(serviceClass: Class<T>?) =
               apply { Companion.serviceClass = serviceClass }*/
        // init Retrofit base url instance
        fun <T> build(mContext: Context, mServiceClass: T, apiUrl: String, timeOut: Long): T {
            val requestInterceptor = Base64EncodeRequestInterceptor(mContext)
            val responseInterceptor = DecryptedPayloadInterceptor(mContext)
            val client = OkHttpClient.Builder().apply {
                addInterceptor(requestInterceptor)
                addInterceptor(responseInterceptor)
                if (BuildConfig.DEBUG)
                    addInterceptor(loggingInterceptor)
                connectTimeout(30, TimeUnit.MINUTES)
                readTimeout(30, TimeUnit.MINUTES)
//                writeTimeout(10, TimeUnit.SECONDS)
            }.build()
            builder = createBuilder(client, apiUrl)
            return builder.create(mServiceClass as Class<T>)
        }

        private fun createBuilder(client: OkHttpClient, apiUrl: String): Retrofit {
            builder = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
            return builder
        }

    }
}