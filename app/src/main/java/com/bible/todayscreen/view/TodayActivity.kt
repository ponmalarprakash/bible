package com.bible.todayscreen.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.commentaryscreen.interfaces.MoveListener
import com.bible.core.AppController
import com.bible.core.CommonViewModelFactory
import com.bible.core.obtainViewModel
import com.bible.databinding.ActivityTodayBinding
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.roomDatabase.AppDatabase
import com.bible.roomDatabase.SampleDataDao
import com.bible.todayscreen.adapter.TodaySlidingAdapter
import com.bible.todayscreen.viewmodel.TodayViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class TodayActivity : AppCompatActivity(), MoveListener {

    lateinit var todayBinding: ActivityTodayBinding

    lateinit var todayViewModel: TodayViewModel

    lateinit var todayDate: AppCompatTextView

    lateinit var backIV: AppCompatImageView

    lateinit var mDao: SampleDataDao
    private lateinit var commentariesData: CommentariesResponse
    var progressDialog: ProgressDialog? = null
    lateinit var homeProgressLoader: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDao = AppDatabase.getInstance(this).sampleDataDao()

        todayBinding =
            DataBindingUtil.setContentView(this@TodayActivity, R.layout.activity_today)

        todayViewModel = obtainViewModel(
            TodayViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(todayBinding)
        {
            lifecycleOwner = this@TodayActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@TodayActivity)
        }

        initialize()
        observeSubscribers()
    }


    private fun initialize() {

        todayDate = findViewById(R.id.todayDate)

        backIV = findViewById(R.id.backIV)

        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        todayDate.text = formattedDate

        homeProgressLoader = findViewById(R.id.homeProgressLoader)

        backIV.setOnClickListener {
            val intent = Intent(this@TodayActivity, BibleHomeActivity::class.java)
            startActivity(intent)
            finish()
        }



        if (AppController.isversesDownloadInitialized()) {
            if (AppController.versesDownload.data.verses.size > 0) {


                todayBinding.viewPager.adapter = TodaySlidingAdapter().apply {
                    setItem(
                        AppController.versesDownload.data.verses,
                        this@TodayActivity
                    )
                }
                TabLayoutMediator(
                    todayBinding.tabLayout,
                    todayBinding.viewPager,
                    TabLayoutMediator.TabConfigurationStrategy { _, _ ->

                    }).attach()

                todayBinding.noDataFound.visibility = View.GONE
                todayBinding.viewPager.visibility = View.VISIBLE
                todayBinding.tabLayout.visibility = View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    homeProgressLoader.visibility = View.GONE


                }, 2000)
            } else {
                todayBinding.noDataFound.visibility = View.VISIBLE
                todayBinding.viewPager.visibility = View.GONE
                todayBinding.tabLayout.visibility = View.GONE
                homeProgressLoader.visibility = View.GONE


            }
        } else {
            todayBinding.noDataFound.visibility = View.VISIBLE
            todayBinding.viewPager.visibility = View.GONE
            todayBinding.tabLayout.visibility = View.GONE
            homeProgressLoader.visibility = View.GONE
        }

    }


    private fun observeSubscribers() {

    }


    override fun onBackPressed() {
        val intent = Intent(this@TodayActivity, BibleHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun moveListener(position: Int) {
    }


}