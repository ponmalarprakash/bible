package com.bible.biblehomeactivity.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.get
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.modeldata.homepage.Chapter
import kotlinx.android.synthetic.main.bookgridview.view.*

class GridViewAdapterChapterList(var context: BibleHomeActivity, var chapter: List<Chapter>) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)


    override fun getCount(): Int {
        return chapter.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view: View =
            (if (position != 0 && convertView != null) convertView else layoutInflater.inflate(
                R.layout.bookgridview,
                parent,
                false
            ))
        val title: AppCompatTextView = view.findViewById(R.id.tvGen)
        title.text = chapter[position].name

        val individualParentLay: LinearLayout =
            view.findViewById(R.id.individualParentLay)

        title.isSelected = chapter[position].isSelected

      /*  Handler(Looper.getMainLooper()).postDelayed({
            if(position == 0) {
                view.individualParentLay.get(0).performClick()

            }
        }, 500)*/



        individualParentLay.setOnClickListener {

            context.loadTitles(chapter[position].id, chapter[position].bookId)

            chapter.forEachIndexed { index, title ->
                title.isSelected = index == position
            }
            notifyDataSetChanged()

        }















        return view
    }


}