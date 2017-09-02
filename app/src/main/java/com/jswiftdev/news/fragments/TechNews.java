package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jswiftdev.news.MainActivity;
import com.jswiftdev.news.R;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.utils.C;
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
    void cacheForView(List<Article> articles) {
        this.techNews = articles;
    }

    @Override
    boolean alreadyHasNews() {
        if (this.techNews == null)
            this.techNews = new ArrayList<>();

        return this.techNews.size() > 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTechNewsChangesListener(this);
    }

    @Override
    public void OnSourceChanged(String newSource) {
        Log.d(C.LOG_TAG, "tech news -> OnSourceChanged");
        sourceChangedMaster(newSource);
    }
}
