package com.snapshop.ga.snapshop.api;

/**
 * Created by Gift on 3/20/2017.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    public class ApiClient {

        public static final String BASE_URL = "http://192.168.1.17:8080/SnapShopService/";
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            if (retrofit==null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
    }

