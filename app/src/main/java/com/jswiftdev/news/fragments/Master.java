package com.jswiftdev.news.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jswiftdev.news.MainActivity;
import com.jswiftdev.news.R;
import com.jswiftdev.news.adapters.ArticlesAdapters;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.network.Api;
import com.jswiftdev.news.network.ServiceGenerator;
import com.jswiftdev.news.network.utils.Response;
import com.jswiftdev.news.utils.C;
import com.jswiftdev.news.utils.interfaces.SourceChangesListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class Master extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = Master.class.getSimpleName() + ": ";
    private String activeCategory;
    private String[] categories;
    private boolean taskRunning = false;

    @BindView(R.id.rv_articles_list)
    RecyclerView rvArticlesList;

    @BindView(R.id.category_tabs)
    TabLayout categoryTabs;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpList();
        setUpNewsCategories();

        getNews("al-jazeera-english");

        swipeRefreshLayout.setOnRefreshListener(this);
        categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((MainActivity) getActivity()).setUpNewsSourcesTabs(categories[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpList() {
        rvArticlesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticlesList.setAdapter(new ArticlesAdapters(getActivity(), Article.listAll(Article.class)));
    }

    public void getNews(final String source) {
        new AsyncTask<String, String, Response>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(C.LOG_TAG, "start fetching news -> " + source);
                swipeRefreshLayout.setRefreshing(true);
                taskRunning = true;
            }

            @Override
            protected Response doInBackground(String... strings) {
                try {
                    retrofit2.Response resp =
                            ServiceGenerator.getClient().create(Api.class)
                                    .getArticles((source != null ? source : "al-jazeera-english")).execute();
                    return (Response) resp.body();
                } catch (Exception e) {
                    Log.e(C.LOG_TAG, TAG + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                taskRunning = false;
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);

                if (response != null) {
                    Log.d(C.LOG_TAG, response.toString());

                    if (response.isSuccessful()) {
                        response.saveArticles();
                        rvArticlesList.setAdapter(new ArticlesAdapters(getActivity(), response.getArticles()));
                    }
                } else {
                    Log.e(C.LOG_TAG, "found null from server");
                }
            }
        }.execute();
    }

    private void setUpNewsCategories() {
        categories = getResources().getStringArray(R.array.article_categories);

        for (String category : categories) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.single_category_tab, null);
            TextView tv = view.findViewById(R.id.tv_tab_name);
            tv.setText(category.toUpperCase());
            categoryTabs.addTab(categoryTabs.newTab().setCustomView(view).setTag(category));
        }
    }


    @Override
    public void onRefresh() {
        getNews(((MainActivity) getActivity()).getActiveSource());
    }

    public void sourceChangedMaster(String newSource) {
        getNews(newSource);
    }

    abstract void cacheForView(List<Article> articles);

    abstract boolean alreadyHasNews();
}
