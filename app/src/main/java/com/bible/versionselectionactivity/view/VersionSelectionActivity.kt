package com.bible.versionselectionactivity.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.core.AppController
import com.bible.core.AppController.Companion.versesDownload
import com.bible.core.CommonData.TOKEN
import com.bible.core.CommonViewModelFactory
import com.bible.core.SessionManager
import com.bible.core.obtainViewModel
import com.bible.databinding.VersionSelectionActLayBinding
import com.bible.splashactivity.view.SplashActivity
import com.bible.versionselectionactivity.adapter.VersionSelectionAdapter
import com.bible.versionselectionactivity.viewmodel.VersionSelectionViewModel

class VersionSelectionActivity : AppCompatActivity(), VersionSelectionAdapter.VersionCheck {

    lateinit var binding: VersionSelectionActLayBinding
    lateinit var versionSelectionViewModel: VersionSelectionViewModel
    lateinit var backIV: AppCompatImageView
    lateinit var recyclerViewVersions: RecyclerView
    lateinit var donebtn: AppCompatButton
    lateinit var versionSelectionProgressLoader: ProgressBar

    lateinit var noDataFound: AppCompatTextView

    var versionID: String = "0"
    var versionName: String = ""
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@VersionSelectionActivity,
            R.layout.version_selection_act_lay
        )
        versionSelectionViewModel = obtainViewModel(
            VersionSelectionViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(binding)
        {
            lifecycleOwner = this@VersionSelectionActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@VersionSelectionActivity)
        }

        initialize()
        observeSubscribers()
    }

    private fun initialize() {

        backIV = findViewById(R.id.backIV)
        donebtn = findViewById(R.id.donebtn)

        versionSelectionProgressLoader = findViewById(R.id.homeProgressLoader)

        recyclerViewVersions = findViewById(R.id.recyclerViewVersions)
        recyclerViewVersions.layoutManager = LinearLayoutManager(this)

        noDataFound = findViewById(R.id.noDataFound)

        backIV.setOnClickListener { view: View? ->
            val intent = Intent(this@VersionSelectionActivity, BibleHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        donebtn.setOnClickListener { view: View? ->

            if (versionID.equals("0")) {
                Toast.makeText(
                    this@VersionSelectionActivity,
                    "Kindly choose any version from list",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                AppController.flag = "0"
                versionSelectionViewModel.getBibleData(
                    SessionManager.getSession(
                        this@VersionSelectionActivity,
                        TOKEN, ""
                    ), versionID
                )


            }

        }



        versionSelectionViewModel.getVesions(
            SessionManager.getSession(
                this@VersionSelectionActivity,
                TOKEN, ""
            )
        )

    }


    private fun observeSubscribers() {
        versionSelectionViewModel.versionSelectionResponse.observe(this, Observer {
            println("versionSelectionResponse $it")
            if (it != null) {

                if (it.status == 200 || it.status == 201) {
                    println("versionSelectionResponse ${it.status}")

                    if (it.versions.size > 0) {
                        val versionSelectionAdapter =
                            VersionSelectionAdapter(
                                this@VersionSelectionActivity,
                                it.versions, this
                            )
                        recyclerViewVersions.adapter = versionSelectionAdapter
                        recyclerViewVersions.visibility = View.VISIBLE
                        noDataFound.visibility = View.GONE


                        Handler(Looper.getMainLooper()).postDelayed({
                            versionSelectionProgressLoader.visibility = View.GONE


                        }, 1000)
                    } else {
                        recyclerViewVersions.visibility = View.GONE
                        noDataFound.visibility = View.VISIBLE
                    }


                } else {
                    versionSelectionProgressLoader.visibility = View.GONE
                    val intent = Intent(this@VersionSelectionActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


        versionSelectionViewModel.refreshData.observe(this, Observer {


            if (it) {
                showProgressDialogWithTitle("downloading please wait...")
            }


        })


        versionSelectionViewModel.bibleHomePageResponse.observe(this, Observer {
            println("biblehomepageresponsesiva $it")
            if (it != null) {


                if (it.status == 200 || it.status == 201) {

                    println("biblehomepageresponsesiva ${it.data.verses}")

                    AppController.versionId = versionID
                    AppController.versionName = versionName

                    versesDownload = it

                    //  println("cheeck:::" + versesDownload)

                    Handler(Looper.getMainLooper()).postDelayed({

                        hideProgressDialogWithTitle()

                        val mainActivityIntent =
                            Intent(this@VersionSelectionActivity, BibleHomeActivity::class.java)
                        startActivity(mainActivityIntent)
                        finish()


                    }, 1000)


                } else {
                    val intent = Intent(this@VersionSelectionActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
        })


    }

    override fun versionSelection(versionID: String, versionName: String) {
        println("version_check:::" + versionID)
        this.versionID = versionID
        this.versionName = versionName

    }

    override fun onBackPressed() {
        val intent = Intent(this@VersionSelectionActivity, BibleHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method to show Progress bar
    private fun showProgressDialogWithTitle(substring: String) {
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        //Without this user can hide loader by tapping outside screen
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage(substring)
        progressDialog?.show()

    }


    // Method to hide/ dismiss Progress bar
    fun hideProgressDialogWithTitle() {
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.dismiss()
    }


}
