package com.bible.biblehomeactivity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bible.R;
import com.bible.biblehomeactivity.adapter.BibleContentAdapter;
import com.bible.core.AppController;
import com.bible.biblehomeactivity.interfaces.GetCallBackInterface;
import com.bible.modeldata.homepage.BibleHomePageResponse;
import com.bible.modeldata.homepage.Verse;

import java.util.ArrayList;
import java.util.List;

public class BibleContentFrag extends Fragment implements GetCallBackInterface {

    private Activity context;
    private RecyclerView bible_content_recycler_view;
    private AppCompatTextView tvNoVerses;
    private BibleHomePageResponse bibleHomePageResponse;
    private BibleContentAdapter bibleContentAdapter;
    private ProgressBar homeProgressLoader;
    private List<Verse> bible_content_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bible_content_frag_lay, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        bible_content_recycler_view = view.findViewById(R.id.bible_content_recycler_view);
        bible_content_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        tvNoVerses = view.findViewById(R.id.tvNoVerses);
        homeProgressLoader = view.findViewById(R.id.homeProgressLoader);


        getBibleContent();

    }

    private void getBibleContent() {


        if (AppController.Companion.isversesDownloadInitialized()) {
            homeProgressLoader.setVisibility(View.VISIBLE);
            if (AppController.Companion.getFlag().equals("1")) {



                bible_content_list = new ArrayList<>();


                if (AppController.versesDownload.getData().getVerses().size() > 0) {


                    for (int i = 0; i < AppController.versesDownload.getData().getVerses().size(); i++) {
                        if (AppController.Companion.getBookId().equalsIgnoreCase(AppController.versesDownload.getData().getVerses().get(i).getBook_id()) &&
                                AppController.Companion.getChapterId().equalsIgnoreCase(AppController.versesDownload.getData().getVerses().get(i).getChapter_id())) {
                            bible_content_list.add(AppController.versesDownload.getData().getVerses().get(i));
                        }
                    }

                    if (bible_content_list.size() > 0) {
                        bibleContentAdapter = new BibleContentAdapter(context, bible_content_list, this);
                        bible_content_recycler_view.setAdapter(bibleContentAdapter);


                        tvNoVerses.setVisibility(View.GONE);


                        new Handler().postDelayed(() -> {
                            bible_content_recycler_view.setVisibility(View.VISIBLE);
                            homeProgressLoader.setVisibility(View.GONE);
                        }, 2000);


                    } else {

                        homeProgressLoader.setVisibility(View.GONE);
                        bible_content_recycler_view.setVisibility(View.GONE);
                        tvNoVerses.setVisibility(View.VISIBLE);
                    }
                } else {
                    homeProgressLoader.setVisibility(View.GONE);
                    bible_content_recycler_view.setVisibility(View.GONE);
                    tvNoVerses.setVisibility(View.VISIBLE);
                }

            } else {
                if (AppController.versesDownload.getData().getVerses().size() > 0) {
                    bibleContentAdapter = new BibleContentAdapter(context, AppController.versesDownload.getData().getVerses(), this);
                    bible_content_recycler_view.setAdapter(bibleContentAdapter);

                    tvNoVerses.setVisibility(View.GONE);

                    new Handler().postDelayed(() -> {
                        bible_content_recycler_view.setVisibility(View.VISIBLE);
                        homeProgressLoader.setVisibility(View.GONE);
                    }, 2000);
                } else {
                    bible_content_recycler_view.setVisibility(View.GONE);
                    tvNoVerses.setVisibility(View.VISIBLE);
                    homeProgressLoader.setVisibility(View.GONE);
                }
            }
        } else {
            bible_content_recycler_view.setVisibility(View.GONE);
            tvNoVerses.setVisibility(View.VISIBLE);
            homeProgressLoader.setVisibility(View.GONE);
        }


    }

    @Override
    public void getCallBackInterface(Verse callBackString, int position, String versesId) {

        // System.out.println("Value::::+siva"+callBackString);


       /* //fragment corinthians
        Fragment fragmentCorinthians = new HomeCorinthiansContentFrag();
        Bundle bundleCorinthians = new Bundle();
        bundleCorinthians.putParcelable("content", callBackString);
        fragmentCorinthians.setArguments(bundleCorinthians);


        FragmentTransaction fragmentCorinthiansTransaction = getParentFragmentManager().beginTransaction();
        fragmentCorinthiansTransaction.replace(R.id.frameCorinthianLay, fragmentCorinthians);
        fragmentCorinthiansTransaction.commit();


        //fragment Receive
        Fragment fragmentReceive = new HomeReceiveContentFrag();
        Bundle bundleReceive = new Bundle();
        bundleReceive.putParcelable("content", callBackString);
        fragmentReceive.setArguments(bundleReceive);


        FragmentTransaction fragmentReceiveTransaction = getParentFragmentManager().beginTransaction();
        fragmentReceiveTransaction.replace(R.id.frameReceiveLay, fragmentReceive);
        fragmentReceiveTransaction.commit();*/


    }


    public void moveToVersePosition(int position) {
        System.out.println("came::::" + position);


       /* new Handler(Looper.getMainLooper()).postDelayed(() -> {
            bible_content_recycler_view.smoothScrollToPosition(8);
            bibleContentAdapter.notifyDataSetChanged();
        },1000);*/


    }
}
