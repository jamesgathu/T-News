package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jswiftdev.news.MainActivity;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.utils.C;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;

import java.util.ArrayList;
import java.util.List;


public class Home extends Master implements SourceChangesListener {
    private List<Article> generalNews;

    public static Home newInstance() {
        Bundle args = new Bundle();
        Home fragment = new Home();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generalNews = new ArrayList<>();
    }

    @Override
    void cacheForView(List<Article> articles) {
        this.generalNews = articles;
    }

    @Override
    boolean alreadyHasNews() {
        if (generalNews == null)
            this.generalNews = new ArrayList<>();

        return this.generalNews.size() > 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setHomeChangesListener(this);
    }

    @Override
    public void OnSourceChanged(String newSource) {
        Log.d(C.LOG_TAG, "home news -> OnSourceChanged");
        sourceChangedMaster(newSource);
    }
}
