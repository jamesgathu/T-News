package com.jswiftdev.news.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jswiftdev.news.MainActivity;
import com.jswiftdev.news.utils.Constants;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;


/**
 * displays a list of articles on the {@link Constants#TAG_GENERAL} tab
 */
public class Home extends Master implements SourceChangesListener {

    public static Home newInstance() {
        Bundle args = new Bundle();
        Home fragment = new Home();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setHomeChangesListener(this);
    }

    @Override
    public void OnSourceChanged(String newSource) {
        Log.d(Constants.LOG_TAG, "home news -> OnSourceChanged");
        /*forward request to {@link Master#sourceChangedMaster}*/
        sourceChangedMaster(newSource);
    }
}
