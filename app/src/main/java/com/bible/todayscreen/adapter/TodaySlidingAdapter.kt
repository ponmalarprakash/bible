package com.bible.todayscreen.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bible.core.AppController.Companion.completeReadingDataSize
import com.bible.databinding.LayoutSliderTodayItemBinding
import com.bible.modeldata.homepage.Verse

class TodaySlidingAdapter : RecyclerView.Adapter<TodaySlidingAdapter.ImageViewHolder>() {
    private var listVerse: List<Verse>? = null


    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutSliderTodayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {


        listVerse?.get(position)
            ?.let { holder.bindVerses(it, mContext, position,position+1) }


    }


    fun setItem(
        mData: List<Verse>,
        context: Context
    ) {

        this.listVerse = mData
        this.mContext = context
        completeReadingDataSize = if (listVerse?.size!! > 100) 100 else listVerse?.size!!

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (listVerse?.size!! > 100) 100 else listVerse?.size!!
    }

    class ImageViewHolder(private val binding: LayoutSliderTodayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindVerses(
            mData: Verse,
            mContext: Context,
            position: Int,positionInc: Int
        ) {
            with(binding) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this.txtDescription.text =
                        Html.fromHtml(mData.verse, Html.FROM_HTML_MODE_LEGACY)

                } else this.txtDescription.text = Html.fromHtml(mData.verse)


                this.txtTitle.text = mData.name
                this.txtSubTitle.text = mData.slug

                this.completeReadingTv.text =
                    "Completing " + positionInc + " off " + completeReadingDataSize

                executePendingBindings()
            }
        }


    }
}