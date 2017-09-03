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
import android.widget.TextView;
import android.widget.Toast;

import com.jswiftdev.news.adapters.ViewPagerAdapter;
import com.jswiftdev.news.fragments.Home;
import com.jswiftdev.news.fragments.TechNews;
import com.jswiftdev.news.models.Source;
import com.jswiftdev.news.network.Api;
import com.jswiftdev.news.network.ServiceGenerator;
import com.jswiftdev.news.network.utils.Response;
import com.jswiftdev.news.utils.Constants;
import com.jswiftdev.news.utils.Utils;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * serves by carrying the viewpager that holds the two main categories
 * called from {@link SignInActivity} upon success sign in
 */
public class MainActivity extends AppCompatActivity {
    /**
     * carries the current source in view for the General news
     */
    private String activeSource;

    /**
     * informs {@link Home} of changes in the content to be shown as per the change in tab selection
     */
    private SourceChangesListener homeChangesListener;

    /**
     * informs {@link TechNews} of changes in the content to be shown as per the change in tab selection
     */
    private SourceChangesListener techNewsChangesListener;

    /**
     * carries the {@link Home} and {@link TechNews} fragments
     */
    @BindView(R.id.view_page)
    ViewPager viewPager;

    /**
     * indicates the current active tab between {@link Home} and {@link TechNews}
     */
    @BindView(R.id.tabs)
    public TabLayout tabMaster;

    /**
     * Indicates the current active source
     * relates to {@link #activeSource}
     */
    @BindView(R.id.sources_tabs)
    TabLayout sourcesTabs;


    /**
     * maintains a list of all possible sources
     */
    private List<Source> allSources;

    /**
     * its content keeps varying depending on the category chosen
     *
     * @see com.jswiftdev.news.fragments.Master#categoryTabs
     */
    private List<Source> sievedSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViewPager();
        getSources();
    }


    /**
     * sets up the {@link #tabMaster} using {@link #viewPager}
     */
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance(), Constants.TAG_GENERAL);
        adapter.addFragment(TechNews.newInstance(), Constants.TAG_TECH_NEWS);

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


    /**
     * controls the content shown by the {@link #sourcesTabs} depending on the chosen
     * category from {@link com.jswiftdev.news.fragments.Master#categories}
     *
     * @param sieve the category of the source to be chosen
     * @see Source#category
     */
    public void setUpNewsSourcesTabs(String sieve) {
        sourcesTabs.removeAllTabs();
        sievedSources = new ArrayList<>();

        activeSource = sieve;

        for (Source source : (allSources != null ? allSources : new ArrayList<Source>())) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.single_category_tab, null);
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

        Log.d(Constants.LOG_TAG, "setUpNewsSourcesTabs sieve -> " + sieve + " \nallSources#: " + (sievedSources != null ?
                sievedSources.size() : null));
    }


    /**
     * fetches the articles sources if not already downloaded
     * if available it uses the ones available in the database
     */
    private void getSources() {
        allSources = Source.listAll(Source.class);
        if (allSources != null && allSources.size() > 0) {
            setUpNewsSourcesTabs(null);
        } else {
            if (!Utils.isConnected(MainActivity.this)) {
                Toast.makeText(MainActivity.this, R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
                return;
            }

            new AsyncTask<String, String, Response>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Response doInBackground(String... params) {
                    try {
                        retrofit2.Response resp = ServiceGenerator.getClient().create(Api.class).getSources("").execute();
                        Log.d(Constants.LOG_TAG, "response -> " + resp.body());

                        return (Response) resp.body();
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, e.getMessage());
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
                    Log.d(Constants.LOG_TAG, "homeChangesListener is null");
                else
                    Log.d(Constants.LOG_TAG, "homeChangesListener is not null");

                if (techNewsChangesListener == null)
                    Log.d(Constants.LOG_TAG, "techNewsChangesListener is null");
                else
                    Log.d(Constants.LOG_TAG, "techNewsChangesListener is not null");

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

    /**
     * returns the current chosen source for the news
     *
     * @return string
     */
    public String getActiveSource() {
        return activeSource;
    }

    /**
     * provide reference for callbacks in {@link Home} fragment
     *
     * @param homeChangesListener for callbacks
     */
    public void setHomeChangesListener(SourceChangesListener homeChangesListener) {
        this.homeChangesListener = homeChangesListener;
    }

    /**
     * provide reference for callbacks in {@link TechNews} fragment
     *
     * @param techNewsChangesListener for callbacks
     */
    public void setTechNewsChangesListener(SourceChangesListener techNewsChangesListener) {
        this.techNewsChangesListener = techNewsChangesListener;
    }
}
