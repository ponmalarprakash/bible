package com.bible.splashactivity.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.core.*
import com.bible.splashactivity.service.SplashService
import com.bible.modeldata.splashpage.SplashResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashRepository private constructor(var mContext:Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SplashRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: SplashRepository(context).also {
                instance = it
            }
        }

        internal val loginJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + loginJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val splashService: SplashService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, SplashService::class.java, CommonData.Base_url)
    }

    fun getTokenApicall(
        deviceID: String,
        callback: TaskCallback<SplashResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<SplashResponse> =
                splashService.getTokenApi(
                    deviceID).awaitResult()) {
                is ApiResult.Ok -> foregroundScope.launch {
                    callback.onComplete(result.value)
                }
                is ApiResult.Error -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
                is ApiResult.Exception -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
            }
        }
    }



}