package com.jswiftdev.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jswiftdev.news.adapters.ViewPagerAdapter;
import com.jswiftdev.news.fragments.Home;
import com.jswiftdev.news.fragments.TechNews;
import com.jswiftdev.news.models.Source;
import com.jswiftdev.news.network.Api;
import com.jswiftdev.news.network.ServiceGenerator;
import com.jswiftdev.news.network.utils.Response;
import com.jswiftdev.news.utils.C;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String activeSource;
    private SourceChangesListener homeChangesListener;
    private SourceChangesListener techNewsChangesListener;

    @BindView(R.id.view_page)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    public TabLayout tabMaster;

    @BindView(R.id.sources_tabs)
    TabLayout sourcesTabs;


    private List<Source> allSources;
    private List<Source> sievedSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViewPager();
        getSources();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance(), C.TAG_GENERAL);
        adapter.addFragment(TechNews.newInstance(), C.TAG_TECH_NEWS);

        viewPager.setAdapter(adapter);
        tabMaster.setupWithViewPager(viewPager);

        tabMaster.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    setUpNewsSourcesTabs("technology");
                } else {
                    setUpNewsSourcesTabs(null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setUpNewsSourcesTabs(String sieve) {
        sourcesTabs.removeAllTabs();
        sievedSources = new ArrayList<>();

        activeSource = sieve;

        for (Source source : (allSources != null ? allSources : new ArrayList<Source>())) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.single_category_tab, null);
            ImageView indicator = view.findViewById(R.id.indicator);
            TextView tv = view.findViewById(R.id.tv_tab_name);
            tv.setText(source.getName().toUpperCase());

            if (sieve != null && source.getCategory().equals(sieve)) {
                sievedSources.add(source);
                sourcesTabs.addTab(sourcesTabs.newTab().setCustomView(view).setTag(source));
            } else if (sieve == null) {
                sourcesTabs.addTab(sourcesTabs.newTab().setCustomView(view).setTag(source));
            }
        }

        if (sourcesTabs.getTabAt(0) != null) {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            sourcesTabs.getTabAt(0).select();
                        }
                    }, 100);
        }

        Log.d(C.LOG_TAG, "setUpNewsSourcesTabs sieve -> " + sieve + " \nallSources#: " + (sievedSources != null ?
                sievedSources.size() : null));
    }


    private void getSources() {
        allSources = Source.listAll(Source.class);
        if (allSources != null && allSources.size() > 0) {
            setUpNewsSourcesTabs(null);
        } else {
            new AsyncTask<String, String, Response>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Response doInBackground(String... params) {
                    try {
                        retrofit2.Response resp = ServiceGenerator.getClient().create(Api.class).getSources("").execute();
                        Log.d(C.LOG_TAG, "response -> " + resp.body());

                        return (Response) resp.body();
                    } catch (Exception e) {
                        Log.e(C.LOG_TAG, e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Response response) {
                    super.onPostExecute(response);

                    if (response != null) {
                        response.saveSources();
                        getSources();
                    } else {
                        Toast.makeText(MainActivity.this, "Could not get allSources", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }

        sourcesTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activeSource = ((sievedSources != null && sievedSources.size() > 0) ? sievedSources : allSources).get(tab.getPosition()).getNameId();

                if (tabMaster.getSelectedTabPosition() == 0)
                    homeChangesListener.OnSourceChanged(activeSource);
                else
                    techNewsChangesListener.OnSourceChanged(activeSource);

                ((ViewGroup) tab.getCustomView()).getChildAt(1).setVisibility(View.VISIBLE);

                if (homeChangesListener == null)
                    Log.d(C.LOG_TAG, "homeChangesListener is null");
                else
                    Log.d(C.LOG_TAG, "homeChangesListener is not null");

                if (techNewsChangesListener == null)
                    Log.d(C.LOG_TAG, "techNewsChangesListener is null");
                else
                    Log.d(C.LOG_TAG, "techNewsChangesListener is not null");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ViewGroup) tab.getCustomView()).getChildAt(1).setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public String getActiveSource() {
        return activeSource;
    }

    public void setHomeChangesListener(SourceChangesListener homeChangesListener) {
        this.homeChangesListener = homeChangesListener;
    }

    public void setTechNewsChangesListener(SourceChangesListener techNewsChangesListener) {
        this.techNewsChangesListener = techNewsChangesListener;
    }
}
