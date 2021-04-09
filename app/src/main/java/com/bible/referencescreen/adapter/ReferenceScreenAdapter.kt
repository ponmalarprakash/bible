package com.bible.referencescreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.databinding.TitlesSelectionAdapterLayBinding
import com.bible.modeldata.referencepage.Title


class ReferenceScreenAdapter(
    private var context: Context,
    private var titles_list: List<Title>, private var listener: TitlesCheck
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TitlesSelectionAdapterLayBinding>(
            layoutInflater,
            R.layout.titles_selection_adapter_lay,
            parent,
            false
        )
        return TitlesListViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return titles_list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        val viewHolderTitlesListAdapter = holder as TitlesListViewHolder
        viewHolderTitlesListAdapter.binding!!.titlesNameTv.text = titles_list[position].title


        if (selectedPosition == position) {
            viewHolderTitlesListAdapter.binding!!.titlesSelectedIv.setImageResource(R.drawable.ic_tick_check_icon)
        } else {
            viewHolderTitlesListAdapter.binding!!.titlesSelectedIv.setImageResource(0)
        }




        viewHolderTitlesListAdapter.binding!!.titlesNameTv.setOnClickListener {

            listener.titleSelection(titles_list[position].bookId, titles_list[position].chapterId,titles_list[position].id)
            selectedPosition = position
            notifyDataSetChanged()


        }


    }


    inner class TitlesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var binding: TitlesSelectionAdapterLayBinding? = null

        init {
            binding = DataBindingUtil.bind(view)
        }
    }

    interface TitlesCheck {
        fun titleSelection(bookID: String, chapterID: String,titleId:String)
    }


}