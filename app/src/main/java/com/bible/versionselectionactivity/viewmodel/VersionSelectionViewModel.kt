package com.bible.versionselectionactivity.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bible.core.BaseViewModel
import com.bible.core.ConnectivityReceiver
import com.bible.core.TaskCallback
import com.bible.core.TaskCallbackProgress
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.versionselectionpage.VersionSelectionResponse
import com.bible.versionselectionactivity.repository.VersionSelectionRepository
import kotlin.math.roundToInt

class VersionSelectionViewModel(
    var versionSelectionRepository: VersionSelectionRepository,
    var context: Application
) :
    BaseViewModel(context) {
    val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading


    private val _refreshData = MutableLiveData<Boolean>()
    val refreshData: LiveData<Boolean>
        get() = _refreshData


    private val _bibleHomePageResponse = MutableLiveData<BibleHomePageResponse>()
    val bibleHomePageResponse: LiveData<BibleHomePageResponse>
        get() = _bibleHomePageResponse

    private val _versionSelectionResponse = MutableLiveData<VersionSelectionResponse>()
    val versionSelectionResponse: LiveData<VersionSelectionResponse>
        get() = _versionSelectionResponse

    fun getVesions(authToken: String) {
        if (ConnectivityReceiver.isOnline) {
            _dataLoading.value = true
            versionSelectionRepository.getVersionSelectionListAPi(
                authToken, object :
                    TaskCallback<VersionSelectionResponse> {

                    override fun onComplete(result: VersionSelectionResponse) {
                        _versionSelectionResponse.value = result
                        _dataLoading.value = false
                    }

                    override fun onException(t: Throwable?) {
                        _dataLoading.value = false
                    }
                })
        }
    }


    fun getBibleData(authtToken: String, versionId: String) {
        if (ConnectivityReceiver.isOnline) {
            _refreshData.value = true
            versionSelectionRepository.getBibleDataApicall(
                authtToken, versionId, object :
                    TaskCallbackProgress<BibleHomePageResponse> {

                    override fun onComplete(result: BibleHomePageResponse) {


                        val data = ByteArray(result.toString().length)
                        var offset = 0

                        val currentRead =
                            result.toString().byteInputStream().read(data, offset, data.size)
                        offset += currentRead
                        val progress = (offset * 100f / data.size).roundToInt()

                        println("siiiiiva::::" + "success" + progress)

                        if (progress == 100) {
                            _bibleHomePageResponse.value = result
                            _refreshData.value = false
                        }


                    }

                    override fun onException(t: Throwable?) {
                        _refreshData.value = false
                    }

                })
        }
    }

}