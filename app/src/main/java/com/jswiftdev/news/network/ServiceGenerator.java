package com.jswiftdev.news.network;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jswiftdev.news.utils.C;
import com.orm.SugarRecord;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient clientBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apiKey", C.API_KEY)
                        .addQueryParameter("language", C.EN)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();


        final SugarExclude strategy = new SugarExclude(SugarRecord.class);

        final Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(strategy)
                .addSerializationExclusionStrategy(strategy)
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder)
                .build();


        return retrofit;
    }

    static class SugarExclude implements ExclusionStrategy {
        private Class<?> clazz;

        public SugarExclude(Class<?> clazzToExclude) {
            this.clazz = clazzToExclude;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(clazz) && f.getName().equals("id");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
