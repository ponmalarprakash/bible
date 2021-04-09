package com.bible.biblehomeactivity.adapter

import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.bible.R
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.modeldata.homepage.Before

class GridViewAdapterBeforeChrist(var context: BibleHomeActivity, var before: List<Before>) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)


    override fun getCount(): Int {
        return before.size
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
        title.text = before[position].short_name
        val individualParentLay: LinearLayout =
            view.findViewById(R.id.individualParentLay)

        title.isSelected = before[position].isSelected



        individualParentLay.setOnClickListener {

            context.loadChapters(before[position].id, before[position].name)



            before.forEachIndexed { index, title ->
                title.isSelected = index == position
            }
            notifyDataSetChanged()

        }






        return view
    }


}