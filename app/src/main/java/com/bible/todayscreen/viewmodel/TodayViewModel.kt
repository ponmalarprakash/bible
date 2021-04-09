package com.bible.todayscreen.viewmodel

import android.app.Application
import com.bible.core.BaseViewModel
import com.bible.todayscreen.repository.TodayRepository

class TodayViewModel(var todayRepository: TodayRepository,
                     var context: Application) :
BaseViewModel(context) {


}
