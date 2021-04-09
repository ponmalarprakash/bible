package com.bible.biblehomeactivity.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bible.R
import com.bible.biblehomeactivity.adapter.GridViewAdapterAfterChrist
import com.bible.biblehomeactivity.adapter.GridViewAdapterBeforeChrist
import com.bible.biblehomeactivity.adapter.GridViewAdapterChapterList
import com.bible.biblehomeactivity.adapter.RecyclerViewAdapterSynopsis
import com.bible.biblehomeactivity.viewmodel.BibleHomeViewModel
import com.bible.core.*
import com.bible.core.AppController.Companion.isversesDownloadInitialized
import com.bible.core.AppController.Companion.versesDownload
import com.bible.core.AppController.Companion.versionId
import com.bible.core.AppController.Companion.versionName
import com.bible.core.CommonData.TOKEN
import com.bible.core.roomDatabase.roomDataTable.VersesTableValue
import com.bible.databinding.BibleHomePageBinding
import com.bible.biblehomeactivity.fragment.BibleContentFrag
import com.bible.commentaryscreen.view.CommentaryActivity
import com.bible.splashactivity.view.SplashActivity
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.referencescreen.view.ReferenceActivity
import com.bible.roomDatabase.AppDatabase
import com.bible.roomDatabase.Converters
import com.bible.roomDatabase.SampleDataDao
import com.bible.todayscreen.view.TodayActivity
import com.bible.versionselectionactivity.view.VersionSelectionActivity
import kotlinx.android.synthetic.main.rightsliderlay.*

class BibleHomeActivity : AppCompatActivity() {

    lateinit var bibleHomeViewModel: BibleHomeViewModel
    lateinit var activityBibleHomePageBinding: BibleHomePageBinding
    lateinit var homeProgressLoader: ProgressBar

    lateinit var beforeChristGridView: GridView
    lateinit var afterChristGridView: GridView

    lateinit var drawer_layout: DrawerLayout

    lateinit var gridViewChapter: GridView

    lateinit var secondLay: FrameLayout
    lateinit var firstLay: FrameLayout

    lateinit var mDao: SampleDataDao
    lateinit var referenceParentLay: LinearLayout

    lateinit var todayParentLay: LinearLayout
    lateinit var commentaryParentLay: LinearLayout

    lateinit var connectionStateMonitor: ConnectionStateMonitor

    var flag: String = "0"


    lateinit var recyclerViewAdapterSynopsis: RecyclerViewAdapterSynopsis
    lateinit var mLayoutManager: LinearLayoutManager
    var progressDialog: ProgressDialog? = null
    var progressDialogVerses: ProgressDialog? = null

    // lateinit internal var versesDownload: BibleHomePageResponse
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDao = AppDatabase.getInstance(this).sampleDataDao()
        activityBibleHomePageBinding =
            DataBindingUtil.setContentView(this, R.layout.bible_home_page)
        bibleHomeViewModel = obtainViewModel(
            BibleHomeViewModel::class.java,
            CommonViewModelFactory(this)
        )
        with(activityBibleHomePageBinding)
        {
            lifecycleOwner = this@BibleHomeActivity
            executePendingBindings()
            progressDialog = ProgressDialog(this@BibleHomeActivity)
            progressDialogVerses = ProgressDialog(this@BibleHomeActivity)
            connectionStateMonitor = ConnectionStateMonitor(this@BibleHomeActivity)
        }
        initialize()
        observeSubscribers()
    }

    private fun initialize() {
        drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val txtCancel = findViewById<AppCompatTextView>(R.id.txtCancel)
        val txtchristTitle = findViewById<AppCompatTextView>(R.id.txtchristTitle)


        val menu_iv = findViewById<ImageView>(R.id.menu_iv)
        val version_name_tv = findViewById<TextView>(R.id.version_name_tv)
        val bookIcon = findViewById<ImageView>(R.id.bookIcon)
        /* val viewStart = findViewById<LinearLayout>(R.id.viewStart)
         val viewEnd = findViewById<LinearLayout>(R.id.viewEnd)*/


        referenceParentLay = findViewById(R.id.referenceParentLay)

        commentaryParentLay = findViewById(R.id.commentaryParentLay)

        todayParentLay = findViewById(R.id.todayParentLay)

        beforeChristGridView = findViewById(R.id.beforeChristGridView)

        afterChristGridView = findViewById(R.id.afterChristGridView)

        gridViewChapter = findViewById(R.id.gridViewChapter)


        homeProgressLoader = findViewById(R.id.homeProgressLoader)

        secondLay = findViewById(R.id.secondLay)

        firstLay = findViewById(R.id.firstLay)


        secondLay.visibility = View.GONE
        firstLay.visibility = View.VISIBLE


        txtCancel.text = resources.getString(R.string.cancel)
        txtchristTitle.text = resources.getString(R.string.books)





        mLayoutManager = LinearLayoutManager(this)
        recyclerViewSynopsis.layoutManager = mLayoutManager




        menu_iv.setOnClickListener { view ->


            drawer_layout.openDrawer(
                GravityCompat.START
            )
        }

        referenceParentLay.setOnClickListener { view ->

            val intent = Intent(this@BibleHomeActivity, ReferenceActivity::class.java)
            startActivity(intent)
            finish()

        }

        commentaryParentLay.setOnClickListener { view ->

            val intent = Intent(this@BibleHomeActivity, CommentaryActivity::class.java)
            startActivity(intent)
            finish()

        }

        todayParentLay.setOnClickListener { view ->

            val intent = Intent(this@BibleHomeActivity, TodayActivity::class.java)
            startActivity(intent)
            finish()

        }



        txtCancel.setOnClickListener { view ->

            if (secondLay.isVisible) {
                secondLay.visibility = View.GONE
                firstLay.visibility = View.VISIBLE

                txtCancel.text = resources.getString(R.string.cancel)
                txtchristTitle.text = resources.getString(R.string.books)
            } else {
                drawer_layout.closeDrawer(GravityCompat.END)
            }


        }

        bookIcon.setOnClickListener { view ->

            txtCancel.text = resources.getString(R.string.cancel)
            txtchristTitle.text = resources.getString(R.string.books)


            flag = "1"
            refreshBookLay()

            drawer_layout.openDrawer(
                GravityCompat.END
            )


        }

        version_name_tv.setOnClickListener { view: View? ->
            val intent = Intent(this@BibleHomeActivity, VersionSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }




        println("versionId:::" + versionId)
        println("versionId::" + versionName)

        refreshBookLay()


        version_name_tv.text = versionName


    }

    private fun refreshBookLay() {
        bibleHomeViewModel.getBooks(
            SessionManager.getSession(
                this@BibleHomeActivity,
                TOKEN, ""
            )
        )
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


        bibleHomeViewModel.bibleHomePageResponse.observe(this, Observer {
            if (it != null) {

                if (it.status == 200 || it.status == 201) {


                    versesDownload = it

                    insertBibleVerses(it).execute()



                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialogWithTitleVerses()

                        val fragment = BibleContentFrag()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.bible_content_frame_lay, fragment, "MainActivity")
                            .commit()


                    }, 2000)


                } else {
                    val intent = Intent(this@BibleHomeActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
        })


        bibleHomeViewModel.refreshData.observe(this, Observer {


            if (it) {
                showProgressDialogWithTitleVerses("downloading please wait...")
            }


        })

        bibleHomeViewModel.fetchData.observe(this, Observer {


            if (it) {
                showProgressDialogWithTitle("fetching please wait...")
            }


        })



        bibleHomeViewModel.bookListResponse.observe(this, Observer {
            println("BooklistResponse $it")
            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    println("BooklistResponse ${it.status}")


                    if (flag == "0")
                        callVersesApi()




                    if (it.books.before.size > 0) {
                        beforeChristGridView.adapter =
                            GridViewAdapterBeforeChrist(this@BibleHomeActivity, it.books.before)
                        beforeChristGridView.visibility = View.VISIBLE
                        noBeforeChristDataFound.visibility = View.GONE


                    } else {
                        beforeChristGridView.visibility = View.GONE
                        noBeforeChristDataFound.visibility = View.VISIBLE
                    }

                    if (it.books.after.size > 0) {
                        afterChristGridView.adapter =
                            GridViewAdapterAfterChrist(this@BibleHomeActivity, it.books.after)
                        afterChristGridView.visibility = View.VISIBLE
                        noAfterChristDataFound.visibility = View.GONE
                    } else {
                        afterChristGridView.visibility = View.GONE
                        noAfterChristDataFound.visibility = View.VISIBLE
                    }



                    secondLay.visibility = View.GONE
                    firstLay.visibility = View.VISIBLE

                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialogWithTitle()
                        println("true_false::" + it)

                    }, 500)


                } else {
                    val intent = Intent(this@BibleHomeActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


        bibleHomeViewModel.chapterListResponse.observe(this, Observer {
            println("chapterListResponse $it")
            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    println("chapterListResponse ${it.status}")


                    secondLay.visibility = View.VISIBLE
                    firstLay.visibility = View.GONE


                    removeViewAll()



                    if (it.chapters.size > 0) {
                        gridViewChapter.adapter =
                            GridViewAdapterChapterList(this@BibleHomeActivity, it.chapters)
                        gridViewChapter.visibility = View.VISIBLE
                        noChapterDataFound.visibility = View.GONE

                    } else {
                        gridViewChapter.visibility = View.GONE
                        noChapterDataFound.visibility = View.VISIBLE
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialogWithTitle()
                        println("true_false::" + it)

                    }, 500)


                } else {
                    val intent = Intent(this@BibleHomeActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })




        bibleHomeViewModel.titlesListResponse.observe(this, Observer {
            println("titleListResponse $it")
            if (it != null) {

                if (it.status == 200 || it.status == 201) {

                    println("titleListResponse ${it.status}")


                    secondLay.visibility = View.VISIBLE
                    firstLay.visibility = View.GONE

                    if (it.titles.size > 0) {
                        noDataFound.visibility = View.GONE
                        recyclerViewSynopsis.visibility = View.VISIBLE

                        recyclerViewAdapterSynopsis = RecyclerViewAdapterSynopsis(
                            this, it.titles
                        )

                        recyclerViewSynopsis.adapter = recyclerViewAdapterSynopsis
                    } else {
                        noDataFound.visibility = View.VISIBLE
                        recyclerViewSynopsis.visibility = View.GONE
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialogWithTitle()
                        println("true_false::" + it)

                    }, 500)


                } else {
                    val intent = Intent(this@BibleHomeActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })


    }


    private inner class insertBibleVerses(var data: BibleHomePageResponse) :
        AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            mDao.insertVersesValue(VersesTableValue(Converters().getVersesValueFromModel(data)))

        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            println("Log check   ___7")

            /*  mDao.getVersesValue().observeForever {
                  Log.e("VersesValueCheck:::", "" + it)

              }*/
        }
    }


    private fun callVersesApi() {
        if (!isversesDownloadInitialized()) {

            AppController.flag = "0"
            bibleHomeViewModel.getBibleData(
                SessionManager.getSession(
                    this@BibleHomeActivity,
                    TOKEN, ""
                ), versionId
            )


        } else {

            if (AppController.flag == "1") {
                val fragment = BibleContentFrag()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.bible_content_frame_lay, fragment, "MainActivity").commit()


            } else {
                AppController.flag = "0"

                val fragment = BibleContentFrag()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.bible_content_frame_lay, fragment, "MainActivity").commit()

            }


        }
    }


    fun loadChapters(bookID: String, christTitle: String) {


        txtchristTitle.text = christTitle
        txtCancel.text = resources.getString(R.string.back)

        bibleHomeViewModel.getChapterList(
            SessionManager.getSession(
                this@BibleHomeActivity,
                TOKEN, ""
            ), bookID
        )

    }

    fun loadVerses(titleId: String, bookID: String, chapterID: String) {

        AppController.flag = "1"

        AppController.bookId = bookID
        AppController.titleId = titleId
        AppController.chapterId = chapterID

        drawer_layout.closeDrawer(GravityCompat.END)


        val fragment = BibleContentFrag()
        supportFragmentManager.beginTransaction()
            .replace(R.id.bible_content_frame_lay, fragment, "MainActivity").commit()

    }


    fun loadTitles(chapterID: String, bookID: String) {

        println("println::" + chapterID)
        println("println11::" + bookID)


        bibleHomeViewModel.getTitlesList(
            SessionManager.getSession(
                this@BibleHomeActivity,
                TOKEN, ""
            ), bookID, chapterID
        )

    }

    // Method to show Progress bar
    private fun showProgressDialogWithTitle(substring: String) {
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage(substring)
        progressDialog?.show()

    }


    // Method to hide/ dismiss Progress bar
    fun hideProgressDialogWithTitle() {
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.dismiss()
    }

    // Method to show Progress bar
    private fun showProgressDialogWithTitleVerses(substring: String) {
        progressDialogVerses?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialogVerses?.setCancelable(false)
        progressDialogVerses?.setMessage(substring)
        progressDialogVerses?.show()

    }


    // Method to hide/ dismiss Progress bar
    fun hideProgressDialogWithTitleVerses() {
        progressDialogVerses!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialogVerses!!.dismiss()
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                "" + getResources().getString(R.string.please_click_back_again_exit),
                Toast.LENGTH_LONG
            ).show()
            Handler(Looper.getMainLooper())
                .postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    fun removeViewAll() {
        noDataFound.visibility = View.VISIBLE
        recyclerViewSynopsis.visibility = View.GONE
    }


}