package com.jswiftdev.news.network.utils;

import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.models.Source;

import java.util.List;

/**
 * marks the overall template for responses from the server
 */
public class Response {
    private String status;
    private String source;
    private String sortBy;

    private List<Article> articles;
    private List<Source> sources;

    public Response(String status, String source, String sortBy, List<Article> articles, List<Source> sources) {
        this.status = status;
        this.source = source;
        this.sortBy = sortBy;
        this.articles = articles;
        this.sources = sources;
    }

    /**
     * should be called only after {@link com.jswiftdev.news.network.Api#getArticles(String)}
     *
     * @return list of articles from the response body
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * should be called only after {@link com.jswiftdev.news.network.Api#getSources(String)} otherwise will return null
     *
     * @return list of sources from the response body
     */
    public List<Source> getSources() {
        return sources;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", articles=" + articles +
                '}';
    }

    /**
     * confirm that the request to the server succeeded
     *
     * @return true if successful
     */
    public boolean isSuccessful() {
        return status.equals("ok");
    }

    /**
     * cache source of news articles
     */
    public void saveSources() {
        if (sources != null) {
            for (Source source : sources) {
                source.save();
            }
        }
    }

    /**
     * cache some article to local storage to show before the next fetch
     */
    public void saveArticles() {
        for (Article article : articles) {
            if (!Article.exists(article.getTitle()))
                article.save();
        }
    }
}
