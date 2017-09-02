package com.jswiftdev.news.models;

import com.orm.SugarRecord;

import java.util.List;

public class Article extends SugarRecord {
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public Article() {
    }

    @Override
    public String toString() {
        return "{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                "}\n";
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public static boolean exists(String title) {
        return Article.find(Article.class, "title = ?", (title != null ? title : "")).size() > 0;
    }
}
