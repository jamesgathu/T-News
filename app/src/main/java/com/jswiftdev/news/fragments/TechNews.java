package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jswiftdev.news.MainActivity;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.utils.Constants;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;

import java.util.ArrayList;
import java.util.List;

public class TechNews extends Master implements SourceChangesListener {
    private List<Article> techNews;

    public static TechNews newInstance() {
        Bundle args = new Bundle();
        TechNews fragment = new TechNews();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryTabs.setVisibility(View.GONE);
        techNews = new ArrayList<>();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTechNewsChangesListener(this);
    }

    @Override
    public void OnSourceChanged(String newSource) {
        Log.d(Constants.LOG_TAG, "tech news -> OnSourceChanged");
        sourceChangedMaster(newSource);
    }
}
