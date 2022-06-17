package com.example.capd.CoordinatesCalculate;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

// retrofit service 인터페이스

public interface GetCoordinatesService {
    @GET("/v2/local/search/keyword.json")
    Call<Coordinates> searchCoordinatesList
            (@Query("query") String query,
             @Query("page") int page,
             @Query("size") int size,
             @Header("Authorization") String apikey);
}
