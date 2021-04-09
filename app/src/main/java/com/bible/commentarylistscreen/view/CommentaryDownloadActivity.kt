package com.bible.commentarylistscreen.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
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
import com.bible.commentarylistfromdb.view.CommentaryDbActivity
import com.bible.commentarylistscreen.adapter.CommentaryListAdapter
import com.bible.commentarylistscreen.viewmodel.CommentaryDownloadViewModel
import com.bible.core.*
import com.bible.core.AppController.Companion.commentariesResponse
import com.bible.core.roomDatabase.roomDataTable.CommentariesTableValue
import com.bible.databinding.CommentaryDownloadScreenBinding
import com.bible.splashactivity.view.SplashActivity
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.roomDatabase.AppDatabase
import com.bible.roomDatabase.Converters
import com.bible.roomDatabase.SampleDataDao

class CommentaryDownloadActivity : AppCompatActivity(),
    CommentaryListAdapter.CommentarySelection {

    lateinit var commentaryDownloadViewModel: CommentaryDownloadViewModel
    lateinit var binding: CommentaryDownloadScreenBinding
    var progressDialog: ProgressDialog? = null
    lateinit var recyclerViewCommentary: RecyclerView
    lateinit var donebtn: AppCompatButton
    lateinit var commentaryDownloadProgressLoader: ProgressBar
    lateinit var backIV: AppCompatImageView
    lateinit var noDataFound: AppCompatTextView

    var commentaryID: String = "0"
    var commentaryTitle: String = "F .B .Meyer"

    lateinit var mDao: SampleDataDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDao = AppDatabase.getInstance(this).sampleDataDao()
        binding = DataBindingUtil.setContentView(
            this@CommentaryDownloadActivity,
            R.layout.commentary_download_screen
        )
        commentaryDownloadViewModel = obtainViewModel(
            CommentaryDownloadViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(binding)
        {
            lifecycleOwner = this@CommentaryDownloadActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@CommentaryDownloadActivity)
        }

        initialize()
        observeSubscribers()
    }

    private fun initialize() {
        recyclerViewCommentary = findViewById(R.id.recyclerViewCommentary)
        recyclerViewCommentary.layoutManager = LinearLayoutManager(this)

        commentaryDownloadProgressLoader = findViewById(R.id.homeProgressLoader)
        noDataFound = findViewById(R.id.noDataFound)

        donebtn = findViewById(R.id.donebtn)
        backIV = findViewById(R.id.backIV)

        commentaryDownloadViewModel.getCommentaryData(
            SessionManager.getSession(
                this@CommentaryDownloadActivity,
                CommonData.TOKEN, ""
            )
        )

        backIV.setOnClickListener { view: View? ->
            val intent = Intent(this@CommentaryDownloadActivity, CommentaryDbActivity::class.java)
            startActivity(intent)
            finish()
        }


        donebtn.setOnClickListener { view: View? ->

            if (commentaryID.equals("0")) {
                Toast.makeText(
                    this@CommentaryDownloadActivity,
                    "Kindly choose any commentary from list",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                AppController.flag = "0"
                commentaryDownloadViewModel.getCommentaries(
                    SessionManager.getSession(
                        this@CommentaryDownloadActivity,
                        CommonData.TOKEN, ""
                    ), commentaryID
                )


            }

        }


    }

    private fun observeSubscribers() {

        commentaryDownloadViewModel.commentaryDownloadResponse.observe(this, Observer {
            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    if (it.commentaries.size > 0) {
                        val commentarySelectionAdapter =
                            CommentaryListAdapter(
                                this@CommentaryDownloadActivity,
                                it.commentaries, this
                            )
                        recyclerViewCommentary.adapter = commentarySelectionAdapter
                        recyclerViewCommentary.visibility = View.VISIBLE
                        noDataFound.visibility = View.GONE


                        Handler(Looper.getMainLooper()).postDelayed({
                            commentaryDownloadProgressLoader.visibility = View.GONE


                        }, 1000)
                    } else {
                        recyclerViewCommentary.visibility = View.GONE
                        noDataFound.visibility = View.VISIBLE
                    }


                } else {
                    commentaryDownloadProgressLoader.visibility = View.GONE
                    val intent = Intent(this@CommentaryDownloadActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


        commentaryDownloadViewModel.refreshData.observe(this, Observer {


            if (it) {
                showProgressDialogWithTitle("downloading please wait...")
            }


        })

        commentaryDownloadViewModel.commentariesResponse.observe(this, Observer {


            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    commentariesResponse = it

                    insertCommentaries(it).execute()
                    println("commentaries:::" + commentariesResponse)

                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialogWithTitle()
                        val intent = Intent(
                            this@CommentaryDownloadActivity,
                            CommentaryDbActivity::class.java
                        )
                        startActivity(intent)
                        finish()


                    }, 2000)
                }
            }


        })


    }


    private inner class insertCommentaries(var data: CommentariesResponse) :
        AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            mDao.insertCommentariesValue(
                CommentariesTableValue(
                    commentaryID, commentaryTitle, Converters().getCommentariesValueFromModel(data)
                )
            )

        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            println("Log check   ___7")

        }
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

    override fun commentarySelection(commentaryId: String, commentaryTitle: String) {

        this.commentaryID = commentaryId
        this.commentaryTitle = commentaryTitle
    }

    override fun onBackPressed() {
        val intent = Intent(this@CommentaryDownloadActivity, CommentaryDbActivity::class.java)
        startActivity(intent)
        finish()
    }
}