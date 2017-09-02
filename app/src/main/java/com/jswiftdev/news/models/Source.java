package com.jswiftdev.news.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * maps to sources from {@link com.jswiftdev.news.utils.Constants#BASE_URL} and <br>
 * acquired through {@link com.jswiftdev.news.network.Api#getSources(String)}
 */
public class Source extends SugarRecord {
    @SerializedName("id")
    private String name_id;

    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;

    public Source() {
    }

    public String getName() {
        return name;
    }

    public String getNameId() {
        return name_id;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Source{" +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                "}\n";
    }
}
