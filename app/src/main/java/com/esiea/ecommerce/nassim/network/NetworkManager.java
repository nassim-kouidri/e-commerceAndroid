package com.esiea.ecommerce.nassim.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static final String BASE_URL = "https://api.escuelajs.co/api/v1/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static RetrofitService getRetrofitService() {
        return getRetrofitInstance().create(RetrofitService.class);
    }
}
