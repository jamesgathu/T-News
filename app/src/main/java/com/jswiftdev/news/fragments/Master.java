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
import com.jswiftdev.news.adapters.ArticlesAdapter;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.network.Api;
import com.jswiftdev.news.network.ServiceGenerator;
import com.jswiftdev.news.network.utils.Response;
import com.jswiftdev.news.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class Master extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = Master.class.getSimpleName() + ": ";
    /**
     * carries list of categories that one can sort the sources using
     *
     * @see com.jswiftdev.news.models.Source#category
     */
    private String[] categories;

    /**
     * displays content as outlined by {@link ArticlesAdapter}
     */
    @BindView(R.id.rv_articles_list)
    RecyclerView rvArticlesList;

    /**
     * displays the {@link #categories} as pertained from @{@link Constants#BASE_URL}
     */
    @BindView(R.id.category_tabs)
    TabLayout categoryTabs;

    /**
     * monitors refreshing behaviour of the {@link #rvArticlesList}
     */
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
        rvArticlesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticlesList.setAdapter(new ArticlesAdapter(getActivity(), Article.listAll(Article.class)));

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


    /**
     * fetches articles from the api and updates the list
     *
     * @param source specifies the {@link com.jswiftdev.news.models.Source#id} to be fetched from
     */
    public void getNews(final String source) {
        new AsyncTask<String, String, Response>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(Constants.LOG_TAG, "start fetching news -> " + source);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Response doInBackground(String... strings) {
                try {
                    retrofit2.Response resp =
                            ServiceGenerator.getClient().create(Api.class)
                                    .getArticles((source != null ? source : "al-jazeera-english")).execute();
                    return (Response) resp.body();
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, TAG + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);

                if (response != null) {
                    Log.d(Constants.LOG_TAG, response.toString());

                    if (response.isSuccessful()) {
                        response.saveArticles();
                        rvArticlesList.setAdapter(new ArticlesAdapter(getActivity(), response.getArticles()));
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "found null from server");
                }
            }
        }.execute();
    }

    /**
     * displays categories tabs borrowing from {@link #categories}
     * also provides custom view for the tabs
     */
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

    /**
     * called from {@link com.jswiftdev.news.utils.interfaces.SourceChangesListener#OnSourceChanged(String)} <br>
     * of inheriting fragments
     *
     * @param newSource as chosen from change in active tab
     */
    public void sourceChangedMaster(String newSource) {
        getNews(newSource);
    }

}
