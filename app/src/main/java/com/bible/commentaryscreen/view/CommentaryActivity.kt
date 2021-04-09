package com.bible.commentaryscreen.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.commentarylistfromdb.view.CommentaryDbActivity
import com.bible.commentaryscreen.adapter.CommentarySlidingAdapter
import com.bible.commentaryscreen.interfaces.MoveListener
import com.bible.commentaryscreen.viewmodel.CommentaryViewModel
import com.bible.core.AppController
import com.bible.core.AppController.Companion.type
import com.bible.core.CommonData.CommentaryTitle
import com.bible.core.CommonViewModelFactory
import com.bible.core.SessionManager
import com.bible.core.obtainViewModel
import com.bible.databinding.ActivityCommentaryBinding
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.roomDatabase.AppDatabase
import com.bible.roomDatabase.Converters
import com.bible.roomDatabase.SampleDataDao
import com.google.android.material.tabs.TabLayoutMediator

class CommentaryActivity : AppCompatActivity(), MoveListener {

    lateinit var commentaryBinding: ActivityCommentaryBinding

    lateinit var commentaryViewModel: CommentaryViewModel

    lateinit var mDao: SampleDataDao
    private lateinit var commentariesData: CommentariesResponse
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDao = AppDatabase.getInstance(this).sampleDataDao()

        commentaryBinding =
            DataBindingUtil.setContentView(this@CommentaryActivity, R.layout.activity_commentary)

        commentaryViewModel = obtainViewModel(
            CommentaryViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(commentaryBinding)
        {
            lifecycleOwner = this@CommentaryActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@CommentaryActivity)
        }

        initialize()
        observeSubscribers()
    }


    private fun initialize() {

        if (SessionManager.getSession(this@CommentaryActivity, CommentaryTitle, "").equals("")) {
            commentaryBinding.txtCommentaryTitle.text = resources.getString(R.string.download)
        } else {
            commentaryBinding.txtCommentaryTitle.text =
                SessionManager.getSession(this@CommentaryActivity, CommentaryTitle, "")
        }

        mDao.getCommentaryData(
            SessionManager.getSession(
                this@CommentaryActivity,
                CommentaryTitle,
                ""
            )
        ).observeForever {
            Log.e("Check:::", "" + it)

            if (it != null) {

                val typeConverter13 = Converters()
                commentariesData = typeConverter13.getModelFromCommentaryString(it)
                Log.e("Check2::", "" + commentariesData)

                if (commentariesData.commentaries.books.size > 0) {
                    commentaryBinding.viewPager.adapter = CommentarySlidingAdapter().apply {
                        setItem(
                            commentariesData,
                            this@CommentaryActivity,
                            this@CommentaryActivity
                            ,
                            type
                        )
                    }
                    TabLayoutMediator(
                        commentaryBinding.tabLayout,
                        commentaryBinding.viewPager,
                        TabLayoutMediator.TabConfigurationStrategy { _, _ ->

                        }).attach()

                    commentaryBinding.noDataFound.visibility = View.GONE
                    commentaryBinding.viewPager.visibility = View.VISIBLE
                    commentaryBinding.tabLayout.visibility = View.VISIBLE
                } else {
                    commentaryBinding.noDataFound.visibility = View.VISIBLE
                    commentaryBinding.viewPager.visibility = View.GONE
                    commentaryBinding.tabLayout.visibility = View.GONE
                }
            } else {
                commentaryBinding.noDataFound.visibility = View.VISIBLE
                commentaryBinding.viewPager.visibility = View.GONE
                commentaryBinding.tabLayout.visibility = View.GONE
            }


        }
        commentaryBinding.txtBook.setOnClickListener {


            type = "B"
            commentaryBinding.txtBook.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.darkPink
                )
            )
            commentaryBinding.txtChapter.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )
            commentaryBinding.txtVerse.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )

if(::commentariesData.isInitialized)

{
    if (commentariesData.commentaries.books.size > 0) {
        commentaryBinding.viewPager.adapter = CommentarySlidingAdapter().apply {
            setItem(
                commentariesData,
                this@CommentaryActivity,
                this@CommentaryActivity
                ,
                type
            )
        }
        TabLayoutMediator(
            commentaryBinding.tabLayout,
            commentaryBinding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { _, _ ->

            }).attach()

        commentaryBinding.noDataFound.visibility = View.GONE
        commentaryBinding.viewPager.visibility = View.VISIBLE
        commentaryBinding.tabLayout.visibility = View.VISIBLE
    } else {
        commentaryBinding.noDataFound.visibility = View.VISIBLE
        commentaryBinding.viewPager.visibility = View.GONE
        commentaryBinding.tabLayout.visibility = View.GONE
    }
}
else {
    commentaryBinding.noDataFound.visibility = View.VISIBLE
    commentaryBinding.viewPager.visibility = View.GONE
    commentaryBinding.tabLayout.visibility = View.GONE
}





        }

        commentaryBinding.txtCommentaryTitle.setOnClickListener {
            val intent = Intent(this@CommentaryActivity, CommentaryDbActivity::class.java)
            startActivity(intent)
            finish()

        }

        commentaryBinding.txtChapter.setOnClickListener {


            type = "C"

            commentaryBinding.txtBook.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )
            commentaryBinding.txtChapter.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.darkPink
                )
            )
            commentaryBinding.txtVerse.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )

            if(::commentariesData.isInitialized)

            {
                if (commentariesData.commentaries.chapters.size > 0) {
                    commentaryBinding.viewPager.adapter = CommentarySlidingAdapter().apply {
                        setItem(
                            commentariesData,
                            this@CommentaryActivity,
                            this@CommentaryActivity
                            ,
                            type
                        )
                    }
                    TabLayoutMediator(
                        commentaryBinding.tabLayout,
                        commentaryBinding.viewPager,
                        TabLayoutMediator.TabConfigurationStrategy { _, _ ->

                        }).attach()

                    commentaryBinding.noDataFound.visibility = View.GONE
                    commentaryBinding.viewPager.visibility = View.VISIBLE
                    commentaryBinding.tabLayout.visibility = View.VISIBLE
                } else {
                    commentaryBinding.noDataFound.visibility = View.VISIBLE
                    commentaryBinding.viewPager.visibility = View.GONE
                    commentaryBinding.tabLayout.visibility = View.GONE
                }
            }
            else {
                commentaryBinding.noDataFound.visibility = View.VISIBLE
                commentaryBinding.viewPager.visibility = View.GONE
                commentaryBinding.tabLayout.visibility = View.GONE
            }




        }

        commentaryBinding.txtVerse.setOnClickListener {


            type = "V"

            commentaryBinding.txtBook.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )
            commentaryBinding.txtChapter.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.black
                )
            )
            commentaryBinding.txtVerse.setTextColor(
                ContextCompat.getColor(
                    this@CommentaryActivity,
                    R.color.darkPink
                )
            )

            if(::commentariesData.isInitialized)

            {
                if (commentariesData.commentaries.verses.size > 0) {
                    commentaryBinding.viewPager.adapter = CommentarySlidingAdapter().apply {
                        setItem(
                            commentariesData,
                            this@CommentaryActivity,
                            this@CommentaryActivity
                            ,
                            type
                        )
                    }
                    TabLayoutMediator(
                        commentaryBinding.tabLayout,
                        commentaryBinding.viewPager,
                        TabLayoutMediator.TabConfigurationStrategy { _, _ ->

                        }).attach()

                    commentaryBinding.noDataFound.visibility = View.GONE
                    commentaryBinding.viewPager.visibility = View.VISIBLE
                    commentaryBinding.tabLayout.visibility = View.VISIBLE
                } else {
                    commentaryBinding.noDataFound.visibility = View.VISIBLE
                    commentaryBinding.viewPager.visibility = View.GONE
                    commentaryBinding.tabLayout.visibility = View.GONE
                }
            }
            else {
                commentaryBinding.noDataFound.visibility = View.VISIBLE
                commentaryBinding.viewPager.visibility = View.GONE
                commentaryBinding.tabLayout.visibility = View.GONE
            }



        }


    }

    private fun observeSubscribers() {

    }

    override fun moveListener(position: Int) {
    }

    override fun onBackPressed() {
        val intent = Intent(this@CommentaryActivity, BibleHomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}