package com.bible.commentarylistfromdb.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.commentarylistfromdb.adapter.CommentaryDbListAdapter
import com.bible.commentarylistfromdb.viewmodel.CommentaryDbViewModel
import com.bible.commentarylistscreen.view.CommentaryDownloadActivity
import com.bible.commentaryscreen.view.CommentaryActivity
import com.bible.core.*
import com.bible.core.CommonData.CommentaryTitle
import com.bible.databinding.CommentaryDbScreenBinding
import com.bible.roomDatabase.AppDatabase
import com.bible.roomDatabase.SampleDataDao

class CommentaryDbActivity : AppCompatActivity(),
    CommentaryDbListAdapter.CommentaryDbSelection {

    lateinit var commentaryDbViewModel: CommentaryDbViewModel
    lateinit var binding: CommentaryDbScreenBinding
    lateinit var recyclerViewDbCommentary: RecyclerView
    lateinit var donebtn: AppCompatButton
    lateinit var backIV: AppCompatImageView
    lateinit var noDataFound: AppCompatTextView
    lateinit var mDao: SampleDataDao

    lateinit var ivDownload: AppCompatImageView

    var commentaryTitle: String = "Download"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDao = AppDatabase.getInstance(this).sampleDataDao()
        binding = DataBindingUtil.setContentView(
            this@CommentaryDbActivity,
            R.layout.commentary_db_screen
        )
        commentaryDbViewModel = obtainViewModel(
            CommentaryDbViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(binding)
        {
            lifecycleOwner = this@CommentaryDbActivity
            executePendingBindings()
        }

        initialize()
        observeSubscribers()
    }

    private fun initialize() {
        recyclerViewDbCommentary = findViewById(R.id.recyclerViewDbCommentary)
        recyclerViewDbCommentary.layoutManager = LinearLayoutManager(this)

        noDataFound = findViewById(R.id.noDataFound)

        donebtn = findViewById(R.id.donebtn)
        backIV = findViewById(R.id.backIV)

        ivDownload= findViewById(R.id.ivDownload)






        backIV.setOnClickListener { view: View? ->
            val intent = Intent(this@CommentaryDbActivity, CommentaryActivity::class.java)
            startActivity(intent)
            finish()
        }

        ivDownload.setOnClickListener { view: View? ->

            val intent = Intent(this@CommentaryDbActivity, CommentaryDownloadActivity::class.java)
            startActivity(intent)
            finish()
        }


        donebtn.setOnClickListener { view: View? ->

            if (commentaryTitle.equals("Download")) {
                Toast.makeText(
                    this@CommentaryDbActivity,
                    "Kindly choose any commentary from list",
                    Toast.LENGTH_LONG
                ).show()
            } else {


                SessionManager.saveSession(this@CommentaryDbActivity, CommentaryTitle, commentaryTitle)
                val intent = Intent(this@CommentaryDbActivity, CommentaryActivity::class.java)
                startActivity(intent)
                finish()


            }

        }


    }

    private fun observeSubscribers() {


        mDao.getCommentaryValue().observeForever {

            if (it != null) {

                if (it.size > 0) {
                    val commentaryDbAdapter =
                        CommentaryDbListAdapter(
                            this@CommentaryDbActivity,
                            it,this
                        )
                    recyclerViewDbCommentary.adapter = commentaryDbAdapter
                    recyclerViewDbCommentary.visibility = View.VISIBLE
                    noDataFound.visibility = View.GONE
                    donebtn.visibility=View.VISIBLE
                }
                else
                {
                    recyclerViewDbCommentary.visibility = View.GONE
                    noDataFound.visibility = View.VISIBLE
                    donebtn.visibility=View.GONE
                }


            }

            else
            {
                recyclerViewDbCommentary.visibility = View.GONE
                noDataFound.visibility = View.VISIBLE
                donebtn.visibility=View.GONE
            }


        }

    }



    override fun onBackPressed() {
        val intent = Intent(this@CommentaryDbActivity, CommentaryActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun commentaryDbSelection(title: String) {
        this.commentaryTitle=title
    }
}