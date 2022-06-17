package com.example.capd.CoordinatesCalculate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// retrofit 객체 생성

public class RetrofitFactory {

    private static RetrofitFactory instance;
    private static final String BASE_URL = "https://dapi.kakao.com";

    public static RetrofitFactory getRetrofit(){
        if (instance == null){
            instance = new RetrofitFactory();
        }
        return instance;
    }

    private RetrofitFactory(){
    }

    public GetCoordinatesService getCoordinatesService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(GetCoordinatesService.class);
    }
}
