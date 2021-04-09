package com.bible.biblehomeactivity.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bible.R;
import com.bible.modeldata.homepage.Verse;

public class HomeCorinthiansContentFrag extends Fragment {


    private Verse verseResponse;
    private LinearLayout contentAndTitleView, noDataView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_corinthians_frag_lay, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvCorinthians = view.findViewById(R.id.tvCorinthians);
        TextView tvCorinthiansTitle = view.findViewById(R.id.tvHeaderCorinthians);
        tvCorinthians.setVisibility(View.VISIBLE);

        contentAndTitleView=view.findViewById(R.id.contentAndTitleView);
        noDataView=view.findViewById(R.id.noDataView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            verseResponse = bundle.getParcelable("content");
           // System.out.println("value::::corianthians" + verseResponse);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (verseResponse != null) {
                if (verseResponse.getIntro().getContent() != null && verseResponse.getIntro().getTitle() != null) {
                    if (verseResponse.getIntro().getContent().length() > 0 && verseResponse.getIntro().getTitle().length() > 0) {
                        contentAndTitleView.setVisibility(View.VISIBLE);
                        noDataView.setVisibility(View.GONE);
                        tvCorinthians.setText(Html.fromHtml(verseResponse.getIntro().getContent(), Html.FROM_HTML_MODE_LEGACY));
                        tvCorinthiansTitle.setText(Html.fromHtml(verseResponse.getIntro().getTitle(), Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        contentAndTitleView.setVisibility(View.GONE);
                        noDataView.setVisibility(View.VISIBLE);

                    }
                } else {
                    contentAndTitleView.setVisibility(View.GONE);
                    noDataView.setVisibility(View.VISIBLE);

                }

            }


            tvCorinthians.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.in_anim));

        } else {
            if (verseResponse != null) {
                if (verseResponse.getIntro().getContent() != null && verseResponse.getIntro().getTitle() != null) {

                    if (verseResponse.getIntro().getContent().length() > 0 && verseResponse.getIntro().getTitle().length() > 0) {
                        contentAndTitleView.setVisibility(View.VISIBLE);
                        noDataView.setVisibility(View.GONE);
                        tvCorinthians.setText(Html.fromHtml(verseResponse.getIntro().getContent()));
                        tvCorinthiansTitle.setText(Html.fromHtml(verseResponse.getIntro().getTitle()));
                    }
                    else {
                        contentAndTitleView.setVisibility(View.GONE);
                        noDataView.setVisibility(View.VISIBLE);

                    }


                }
                else {
                    contentAndTitleView.setVisibility(View.GONE);
                    noDataView.setVisibility(View.VISIBLE);

                }
            }

            tvCorinthians.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.in_anim));


        }


    }


}
