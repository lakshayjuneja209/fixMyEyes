package com.example.sherlock.fixmyeyes;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofit_service {
    public static String API_BASE_URL_1 = "http://35.196.121.21/index.php/welcome/";

    public static final API get_service() {
        HttpLoggingInterceptor logging;
        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        API client;
        Retrofit.Builder builder =
                new Retrofit.Builder().baseUrl(API_BASE_URL_1)
                        .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        client = retrofit.create(API.class);
        return client;
    }


}
