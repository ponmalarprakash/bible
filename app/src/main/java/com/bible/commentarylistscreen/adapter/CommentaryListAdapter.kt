package com.bible.commentarylistscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.databinding.CommentarySelectionAdapterLayBinding
import com.bible.modeldata.commentarydownloadpage.Commentary


class CommentaryListAdapter(
    private var context: Context,
    private var commentary_list: List<Commentary>, private var listener: CommentarySelection
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CommentarySelectionAdapterLayBinding>(
            layoutInflater,
            R.layout.commentary_selection_adapter_lay,
            parent,
            false
        )
        return CommentaryViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return commentary_list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        val viewHolderCommentaryDownloadAdapter = holder as CommentaryViewHolder
        viewHolderCommentaryDownloadAdapter.binding!!.commentaryTitle.text = commentary_list[position].title


        if (selectedPosition == position) {
            viewHolderCommentaryDownloadAdapter.binding!!.commentarySelectedIv.setImageResource(R.drawable.ic_tick_check_icon)
        } else {
            viewHolderCommentaryDownloadAdapter.binding!!.commentarySelectedIv.setImageResource(0)
        }




        viewHolderCommentaryDownloadAdapter.binding!!.commentaryTitle.setOnClickListener {

            listener.commentarySelection(commentary_list[position].id, commentary_list[position].title)
            selectedPosition = position
            notifyDataSetChanged()


        }


    }


    inner class CommentaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var binding: CommentarySelectionAdapterLayBinding? = null

        init {
            binding = DataBindingUtil.bind(view)
        }
    }

    interface CommentarySelection {
        fun commentarySelection(commentaryID: String, title: String)
    }


}