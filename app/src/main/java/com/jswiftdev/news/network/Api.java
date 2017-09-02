package com.jswiftdev.news.network;


import com.jswiftdev.news.network.utils.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api {

    /**
     * get articles from a given source
     *
     * @param source for the news
     * @return articles
     */
    @GET("articles")
    Call<Response> getArticles(@Query("source") String source);

    /**
     * get sources for the articles
     *
     * @param category of the news
     * @return list of sources
     */
    @GET("sources")
    Call<Response> getSources(@Query("category") String category);

}
