package com.bible.referencescreen.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.core.*
import com.bible.modeldata.referencepage.ReferenceScreenResponse
import com.bible.referencescreen.service.ReferenceScreenService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReferenceRepository private constructor(var mContext: Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ReferenceRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: ReferenceRepository(context).also {
                instance = it
            }
        }

        internal val referenceScreenJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + referenceScreenJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val referenceService: ReferenceScreenService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, ReferenceScreenService::class.java, CommonData.Base_url)
    }


    fun getTitlesListAPi(
        authToken: String,versionId:String,
        callback: TaskCallback<ReferenceScreenResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<ReferenceScreenResponse> =
                referenceService.getTitleListApi(
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



