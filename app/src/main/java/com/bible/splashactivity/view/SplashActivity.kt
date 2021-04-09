package com.bible.splashactivity.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.core.CommonData.TOKEN
import com.bible.core.CommonViewModelFactory
import com.bible.core.ConnectionStateMonitor
import com.bible.core.SessionManager
import com.bible.core.obtainViewModel
import com.bible.databinding.ActivitySplashBinding
import com.bible.splashactivity.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {
    lateinit var splashViewModel: SplashViewModel
    lateinit var activitySplashBinding: ActivitySplashBinding

    lateinit var connectionStateMonitor: ConnectionStateMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        splashViewModel = obtainViewModel(
            SplashViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(activitySplashBinding)
        {
            lifecycleOwner = this@SplashActivity
            executePendingBindings()
            connectionStateMonitor = ConnectionStateMonitor(this@SplashActivity)
        }

        initialize()
        observeSubscribers()

    }

    private fun initialize() {

        splashViewModel.getToken("123456")

    }

    private fun observeSubscribers() {

        connectionStateMonitor.observe(this, Observer<Boolean?>
        {

            if (it!!) {
                println("net_connnection:" + it)
                connectionStateMonitor.hideNetworkView()
            } else {
                println("net_connnection:" + it)
                connectionStateMonitor.noNetworkConnection()
            }


        })


        splashViewModel.splashResponse.observe(this, Observer {
            println("loginResponse $it")
            if (it != null) {
                println("loginResponse ${it.token}")

                SessionManager.saveSession(this@SplashActivity, TOKEN, it.token)


                Handler(Looper.getMainLooper()).postDelayed({
                    val intentMainActivity =
                        Intent(this@SplashActivity, BibleHomeActivity::class.java)
                    startActivity(intentMainActivity)
                    finish()
                }, 1000)


            }
        })
    }
}