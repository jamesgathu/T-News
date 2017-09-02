package com.jswiftdev.news.network.utils;

import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.models.Source;

import java.util.List;


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

    public List<Article> getArticles() {
        return articles;
    }

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

    public boolean isSuccessful() {
        return status.equals("ok");
    }

    public void saveSources() {
        if (sources != null) {
            for (Source source : sources) {
                source.save();
            }
        }
    }

    public void saveArticles() {
        for (Article article : articles) {
            if (!Article.exists(article.getTitle()))
                article.save();
        }
    }
}
