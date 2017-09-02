package com.jswiftdev.news.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

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
