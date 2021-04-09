package com.bible.versionselectionactivity.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.core.*
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.versionselectionpage.VersionSelectionResponse
import com.bible.versionselectionactivity.service.VersionSelectionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VersionSelectionRepository private constructor(var mContext:Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: VersionSelectionRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: VersionSelectionRepository(context).also {
                instance = it
            }
        }

        internal val versionSelectionJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + versionSelectionJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val versionService: VersionSelectionService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, VersionSelectionService::class.java, CommonData.Base_url)
    }

   /* private val versionServiceNew: VersionSelectionService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, VersionSelectionService::class.java, CommonData.Base_url_new)
    }*/

    fun getVersionSelectionListAPi(
        authToken: String,
        callback: TaskCallback<VersionSelectionResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<VersionSelectionResponse> =
                versionService.getSloganListApi(
                    "Bearer "+authToken).awaitResult()) {
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


    fun getBibleDataApicall(
        authToken: String,versionId:String,
        callback: TaskCallbackProgress<BibleHomePageResponse>
    ) {
       backgroundScope.launch {
            when (val result: ApiResult<BibleHomePageResponse> =
                versionService.getBibleData(
                    "Bearer "+authToken,versionId).awaitResult()) {
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