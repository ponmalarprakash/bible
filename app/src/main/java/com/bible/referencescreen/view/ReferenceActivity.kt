package com.bible.referencescreen.view

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
import com.bible.core.*
import com.bible.databinding.ReferenceScreenBinding
import com.bible.splashactivity.view.SplashActivity
import com.bible.referencescreen.adapter.ReferenceScreenAdapter
import com.bible.referencescreen.viewmodel.ReferenceScreenViewModel

class ReferenceActivity : AppCompatActivity(), ReferenceScreenAdapter.TitlesCheck {

    lateinit var binding: ReferenceScreenBinding
    lateinit var referenceScreenViewModel: ReferenceScreenViewModel
    lateinit var backIV: AppCompatImageView
    lateinit var recyclerViewTitles: RecyclerView
    lateinit var donebtn: AppCompatButton
    lateinit var referenceScreenProgressLoader: ProgressBar
    var progressDialog: ProgressDialog? = null

    var bookID: String = ""
    lateinit var noDataFound: AppCompatTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ReferenceActivity,
            R.layout.reference_screen
        )
        referenceScreenViewModel = obtainViewModel(
            ReferenceScreenViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(binding)
        {
            lifecycleOwner = this@ReferenceActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@ReferenceActivity)
        }

        initialize()
        observeSubscribers()
    }

    private fun initialize() {

        backIV = findViewById(R.id.backIV)
        donebtn = findViewById(R.id.donebtn)

        referenceScreenProgressLoader = findViewById(R.id.homeProgressLoader)

        recyclerViewTitles = findViewById(R.id.recyclerViewTitles)
        recyclerViewTitles.layoutManager = LinearLayoutManager(this)

        noDataFound = findViewById(R.id.noDataFound)

        backIV.setOnClickListener { view: View? ->
            val intent = Intent(this@ReferenceActivity, BibleHomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        donebtn.setOnClickListener { view: View? ->


            if (bookID.equals("")) {
                Toast.makeText(
                    this@ReferenceActivity,
                    "Kindly choose any chapters from list",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                AppController.flag = "1"
                val intent = Intent(this@ReferenceActivity, BibleHomeActivity::class.java)
                startActivity(intent)
                finish()


            }

        }




        referenceScreenViewModel.getTitles(
            SessionManager.getSession(
                this@ReferenceActivity,
                CommonData.TOKEN, ""
            ), AppController.versionId
        )


    }


    private fun observeSubscribers() {
        referenceScreenViewModel.referenceScreenResponse.observe(this, Observer {
            println("referenceScreenResponse $it")
            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    if (it.titles.size > 0) {
                        println("referenceScreenResponse ${it.status}")
                        val referenceScreenAdapter =
                            ReferenceScreenAdapter(
                                this@ReferenceActivity,
                                it.titles, this
                            )
                        recyclerViewTitles.adapter = referenceScreenAdapter
                        recyclerViewTitles.visibility = View.VISIBLE
                        noDataFound.visibility = View.GONE
                        donebtn.visibility = View.VISIBLE


                    } else {
                        recyclerViewTitles.visibility = View.GONE
                        noDataFound.visibility = View.VISIBLE
                        donebtn.visibility = View.GONE

                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        referenceScreenProgressLoader.visibility = View.GONE

                    }, 1000)


                } else {
                    referenceScreenProgressLoader.visibility = View.GONE
                    val intent = Intent(this@ReferenceActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


        referenceScreenViewModel.refreshData.observe(this, Observer {


            if (it) {
                showProgressDialogWithTitle("downloading please wait...")
            }


        })


    }

    override fun titleSelection(bookID: String, chapterID: String, titleId: String) {

        AppController.bookId = bookID
        AppController.titleId = titleId
        AppController.chapterId = chapterID
        this.bookID = bookID

    }

    override fun onBackPressed() {
        val intent = Intent(this@ReferenceActivity, BibleHomeActivity::class.java)
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