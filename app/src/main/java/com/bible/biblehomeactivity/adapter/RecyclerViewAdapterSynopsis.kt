package com.bible.biblehomeactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.databinding.BookRecyclerviewItemBinding
import com.bible.modeldata.homepage.Title


class RecyclerViewAdapterSynopsis(
    private val mContext: BibleHomeActivity,
    var titles: List<Title>
) : RecyclerView.Adapter<RecyclerViewAdapterSynopsis.CustomViewHolder>() {
    lateinit var bookRecyclerViewItemBinding: BookRecyclerviewItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        LayoutInflater.from(mContext)
        bookRecyclerViewItemBinding =
            BookRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(bookRecyclerViewItemBinding)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        bookRecyclerViewItemBinding.tvTitle.text = titles[position].title

        bookRecyclerViewItemBinding.titleParentLay.setOnClickListener {

            mContext.loadVerses(titles[position].id,titles[position].bookId,titles[position].chapterId)

        }

    }

    inner class CustomViewHolder(v: BookRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(v.root) {
        var bookRecyclerViewItemBinding: BookRecyclerviewItemBinding

        init {
            bookRecyclerViewItemBinding = v
        }


    }


}

