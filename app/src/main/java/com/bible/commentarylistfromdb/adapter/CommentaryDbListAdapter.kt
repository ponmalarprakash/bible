package com.bible.commentarylistfromdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.databinding.CommentaryDbAdapterLayBinding


class CommentaryDbListAdapter(
    private var context: Context,
    private var commentary_list: List<String>,private var listener: CommentaryDbListAdapter.CommentaryDbSelection
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CommentaryDbAdapterLayBinding>(
            layoutInflater,
            R.layout.commentary_db_adapter_lay,
            parent,
            false
        )
        return CommentaryViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return commentary_list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val viewHolderCommentaryDbAdapter = holder as CommentaryViewHolder
        viewHolderCommentaryDbAdapter.binding!!.commentaryTitle.text =
            commentary_list[position]


        if (selectedPosition == position) {
            viewHolderCommentaryDbAdapter.binding!!.commentarySelectedIv.setImageResource(R.drawable.ic_tick_check_icon)
        } else {
            viewHolderCommentaryDbAdapter.binding!!.commentarySelectedIv.setImageResource(0)
        }




        viewHolderCommentaryDbAdapter.binding!!.commentaryTitle.setOnClickListener {
            listener.commentaryDbSelection(commentary_list[position])
            selectedPosition = position
            notifyDataSetChanged()


        }


    }


    inner class CommentaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var binding: CommentaryDbAdapterLayBinding? = null

        init {
            binding = DataBindingUtil.bind(view)
        }
    }
    interface CommentaryDbSelection {
        fun commentaryDbSelection(title: String)
    }



}