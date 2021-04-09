package com.bible.versionselectionactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bible.R
import com.bible.databinding.VersionSelectionAdapterLayBinding
import com.bible.modeldata.versionselectionpage.Version


class VersionSelectionAdapter(
    private var context: Context,
    private var versions_list: List<Version>, private var listener: VersionCheck
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<VersionSelectionAdapterLayBinding>(
            layoutInflater,
            R.layout.version_selection_adapter_lay,
            parent,
            false
        )
        return VersionListViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return versions_list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        /*val viewHolderVersionListAdapter = holder as VersionListViewHolder

        with(viewHolderVersionListAdapter.binding!!) {

            versionNameTv.text = versions_list[position].name

            if (selectedPosition == position) {
                versionSelectedIv.setImageResource(R.drawable.ic_tick_check_icon)
            } else {
                versionSelectedIv.setImageResource(0)
            }

            versionNameTv.setOnClickListener {

                listener.versionSelection(versions_list[position].id, versions_list[position].name)
                selectedPosition = position
                notifyDataSetChanged()


            }

            progressBarDocument!!.isVisible = versions_list[position].isDownloading
            textProgress!!.isVisible = versions_list[position].isDownloading

        }*/


        val viewHolderVersionListAdapter = holder as VersionListViewHolder
        viewHolderVersionListAdapter.binding!!.versionNameTv.text = versions_list[position].name


        if (selectedPosition == position) {
            viewHolderVersionListAdapter.binding!!.versionSelectedIv.setImageResource(R.drawable.ic_tick_check_icon)
        } else {
            viewHolderVersionListAdapter.binding!!.versionSelectedIv.setImageResource(0)
        }




        viewHolderVersionListAdapter.binding!!.versionNameTv.setOnClickListener {

            listener.versionSelection(versions_list[position].id, versions_list[position].name)
            selectedPosition = position
            notifyDataSetChanged()


        }


    }


    inner class VersionListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var binding: VersionSelectionAdapterLayBinding? = null

        init {
            binding = DataBindingUtil.bind(view)
        }
    }

    interface VersionCheck {
        fun versionSelection(versionID: String, versionName: String)
    }


}