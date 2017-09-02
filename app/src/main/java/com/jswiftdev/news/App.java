package com.jswiftdev.news;

import com.orm.SugarApp;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by james on 9/2/17.
 * manage application level requirements
 */

public class App extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
