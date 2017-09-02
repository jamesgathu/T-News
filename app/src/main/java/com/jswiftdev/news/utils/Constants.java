package com.jswiftdev.news.utils;

import com.jswiftdev.news.network.ServiceGenerator;

/**
 * Created by james on 8/31/17.
 * <p>
 * maintain a list of constants in use throughout the application
 */

public class Constants {
    /**
     * base url for the API server
     * used here {@link ServiceGenerator#getClient()}
     */
    public static final String BASE_URL = "https://newsapi.org/v1/";

    /**
     * API key for authentication with server
     * used here {@link ServiceGenerator#getClient()}
     */
    public static final String API_KEY = "f4e73da41a1448429ff2ad70f67e6624";

    /**
     * specify the language to be used in fetching the news to be returned
     * used here {@link ServiceGenerator#getClient()}
     */
    public static final String EN = "en";

    /**
     * specifies the logcat search term to be used
     */
    public static final String LOG_TAG = "JSWIFTDEV::";

    /**
     * key for carrying url from {@link com.jswiftdev.news.MainActivity} to {@link com.jswiftdev.news.ArticlesPage}
     */
    public static final String KEY_URL = "key_url";

    /**
     * the date format received from the news api server
     * used here {@link Utils#getRelativeTime(String)}
     */
    public static final String SERVER_DATE_FORMAT = "YYYY-MM-dd'T'HH:mm:sss'Z'";

    /**
     * label for the {@link com.jswiftdev.news.fragments.Home} fragment
     */
    public static String TAG_GENERAL = "GENERAL";

    /**
     * label for the {@link com.jswiftdev.news.fragments.TechNews} fragment
     */
    public static final String TAG_TECH_NEWS = "TECH NEWS";
}
