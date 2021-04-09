package com.bible.todayscreen.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.core.*
import com.bible.todayscreen.service.TodayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TodayRepository private constructor(var mContext: Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: TodayRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: TodayRepository(context).also {
                instance = it
            }
        }

        internal val todayJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + todayJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val todayService: TodayService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, TodayService::class.java, CommonData.Base_url)
    }












}