package com.bible.splashactivity.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bible.core.BaseViewModel
import com.bible.core.ConnectivityReceiver
import com.bible.core.TaskCallback
import com.bible.splashactivity.repository.SplashRepository
import com.bible.modeldata.splashpage.SplashResponse

class SplashViewModel(var splashRepository: SplashRepository, var context: Application) :
    BaseViewModel(context) {
    private val _dataLoading = MutableLiveData<Boolean>()

    private val _splashResponse = MutableLiveData<SplashResponse>()
    val splashResponse: LiveData<SplashResponse>
        get() = _splashResponse

    fun getToken(deviceID: String) {
        if (ConnectivityReceiver.isOnline) {
            _dataLoading.value = true
            splashRepository.getTokenApicall(
                deviceID, object :
                    TaskCallback<SplashResponse> {

                    override fun onComplete(result: SplashResponse) {


                        _dataLoading.value = false
                        _splashResponse.value = result
                    }

                    override fun onException(t: Throwable?) {
                        _dataLoading.value = false
                    }
                })
        }
    }

}