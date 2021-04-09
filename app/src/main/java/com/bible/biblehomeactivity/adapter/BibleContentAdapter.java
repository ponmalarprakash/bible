package com.bible.biblehomeactivity.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bible.R;
import com.bible.biblehomeactivity.fragment.BibleContentFrag;
import com.bible.biblehomeactivity.interfaces.GetCallBackInterface;
import com.bible.modeldata.homepage.Verse;


import java.util.List;

public class BibleContentAdapter extends RecyclerView.Adapter<BibleContentAdapter.ViewHolder> {

    private Activity context;
    private List<Verse> bible_content_list;
    private GetCallBackInterface getCallBackInterface;
    private int last_position = 0;

    public BibleContentAdapter(Activity context, List<Verse> bible_content_list, BibleContentFrag bibleContentFrag) {
        this.context = context;
        this.bible_content_list = bible_content_list;
        this.getCallBackInterface = bibleContentFrag;
    }

    @NonNull
    @Override
    public BibleContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bible_content_adapter_lay, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BibleContentAdapter.ViewHolder holder, final int position) {
         holder.bible_section_tv.setText(bible_content_list.get(position).getName());
        holder.bible_section_tv.setPaintFlags(holder.bible_section_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.bible_content_tv.setText(/*bible_content_list.get(position).getName() + " " +*/ Html.fromHtml(bible_content_list.get(position).getVerse().trim(), Html.FROM_HTML_MODE_LEGACY));
        } else
            holder.bible_content_tv.setText(/*bible_content_list.get(position).getName() + " " +*/ Html.fromHtml(bible_content_list.get(position).getVerse().trim()));


        holder.bible_content_lay.setOnClickListener(view -> {
            last_position = position;
            notifyDataSetChanged();
        });

        if (last_position == position) {
            holder.bible_content_lay.setBackgroundColor(context.getResources().getColor(R.color.common_bg_color));
            // getCallBackInterface.getCallBackInterface(bible_content_list.get(position), position, bible_content_list.get(position).getId());
        } else {
            holder.bible_content_lay.setBackgroundColor(context.getResources().getColor(R.color.reading_content_bg));
        }

    }

    @Override
    public int getItemCount() {
        return bible_content_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bible_section_tv, bible_content_tv;
        private LinearLayout bible_content_lay;

        public ViewHolder(@NonNull View view) {
            super(view);
            bible_content_lay = view.findViewById(R.id.bible_content_lay);
            bible_section_tv = view.findViewById(R.id.bible_section_tv);
            bible_content_tv = view.findViewById(R.id.bible_content_tv);
        }
    }


}
