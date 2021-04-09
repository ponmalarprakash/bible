package com.bible.biblehomeactivity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bible.R;
import com.bible.biblehomeactivity.adapter.GridViewAdapter;
import com.bible.biblehomeactivity.interfaces.ScrollToVersePosition;
import com.bible.modeldata.homepage.Data;

public class HomeReceiveContentFrag extends Fragment implements ScrollToVersePosition {

    private GridView bible_verse_view,bible_word_view;
    private Data verseResponse;
    private Activity context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_receive_frag_lay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();


        bible_verse_view = view.findViewById(R.id.bible_verse_view);
        bible_word_view=view.findViewById(R.id.bible_word_view);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            verseResponse = bundle.getParcelable("content");
            getBibleContent();
        }






    }

    private void getBibleContent() {


        GridViewAdapter bible_verse_view_adapter = new GridViewAdapter(context, verseResponse.getVerse_reference(),verseResponse.getWord_reference(),0,this);
        bible_verse_view.setAdapter(bible_verse_view_adapter);

        GridViewAdapter bible_word_view_adapter = new GridViewAdapter(context, verseResponse.getVerse_reference(),verseResponse.getWord_reference(),1,this);
        bible_word_view.setAdapter(bible_word_view_adapter);


    }


    @Override
    public void moveToVersePosition(int position) {

       // System.out.println("sivvvvaa::::"+position);

        BibleContentFrag frag = (BibleContentFrag)
                getParentFragmentManager().findFragmentByTag("MainActivity");
        if (frag != null) {
            frag.moveToVersePosition(position);
        }


    }
}
