package com.bible.referencescreen.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bible.core.BaseViewModel
import com.bible.core.ConnectivityReceiver
import com.bible.core.TaskCallback
import com.bible.core.TaskCallbackProgress
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.referencepage.ReferenceScreenResponse
import com.bible.modeldata.versionselectionpage.VersionSelectionResponse
import com.bible.referencescreen.repository.ReferenceRepository
import com.bible.versionselectionactivity.repository.VersionSelectionRepository
import kotlin.math.roundToInt

class ReferenceScreenViewModel(
    var referenceRepository: ReferenceRepository,
    var context: Application
) :
    BaseViewModel(context) {
    val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading


    private val _refreshData = MutableLiveData<Boolean>()
    val refreshData: LiveData<Boolean>
        get() = _refreshData




    private val _referenceScreenResponse = MutableLiveData<ReferenceScreenResponse>()
    val referenceScreenResponse: LiveData<ReferenceScreenResponse>
        get() = _referenceScreenResponse

    fun getTitles(authToken: String,versioID:String) {
        if (ConnectivityReceiver.isOnline) {
            _dataLoading.value = true
            referenceRepository.getTitlesListAPi(
                authToken,versioID, object :
                    TaskCallback<ReferenceScreenResponse> {

                    override fun onComplete(result: ReferenceScreenResponse) {
                        _referenceScreenResponse.value = result
                        _dataLoading.value = false
                    }

                    override fun onException(t: Throwable?) {
                        _dataLoading.value = false
                    }
                })
        }
    }



}