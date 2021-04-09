package com.bible.biblehomeactivity.adapter

import android.content.Context
import android.graphics.Paint
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.bible.R
import com.bible.biblehomeactivity.fragment.HomeReceiveContentFrag
import com.bible.biblehomeactivity.interfaces.ScrollToVersePosition
import com.bible.modeldata.homepage.VerseReference
import com.bible.modeldata.homepage.WordReference

class GridViewAdapter(
    var context: Context,
    var bible_verse_list: List<VerseReference>,
    var bible_word_list: List<WordReference>,
    var Flag: Int, homeReceiveContentFrag: HomeReceiveContentFrag
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private var scrollToVersePosition:ScrollToVersePosition


    init {
        scrollToVersePosition=homeReceiveContentFrag
    }


    override fun getCount(): Int {
        if (Flag == 0)
            return bible_verse_list.size
        else
            return bible_word_list.size

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
                R.layout.gridrecyclerview,
                parent,
                false
            ))
        val tvVerseName: AppCompatTextView = view.findViewById(R.id.tvVerseName)
        val verse_word_parent_lay: LinearLayout = view.findViewById(R.id.verse_word_parent_lay)

        if (Flag == 0) {
            tvVerseName.text = bible_verse_list.get(position).name
            tvVerseName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        } else {
            tvVerseName.text = bible_word_list.get(position).name
            tvVerseName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }


        verse_word_parent_lay.setOnClickListener {
          /*  if (Flag == 0) {
                scrollToVersePosition.moveToVersePosition(position)
            } else {
                val layoutInflater =
                    context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val inflatedLayoutView = layoutInflater.inflate(R.layout.pop_window, null)
                val width = (context.getResources().getDisplayMetrics().heightPixels * 0.35).roundToInt()
                val height = (context.getResources().getDisplayMetrics().heightPixels * 0.50).roundToInt()
                val focusable = true

                val tvTitle: AppCompatTextView = inflatedLayoutView.findViewById(R.id.tvTitle)
                tvTitle.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                tvTitle.text=bible_word_list[position].name

                val tvMeaning: AppCompatTextView = inflatedLayoutView.findViewById(R.id.tvMeaning)

                tvMeaning.text = bible_word_list[position].meaning


                val popup_l = PopupWindow(inflatedLayoutView, width, height, focusable)
                popup_l.setAnimationStyle(R.style.DialogStyle)
                popup_l.showAtLocation(inflatedLayoutView, Gravity.CENTER, 0, 0)
                popup_l.setOutsideTouchable(false)
            }*/


        }


        return view
    }



}