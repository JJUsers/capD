package com.example.capd.Navigation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PostNavigationService {

    @FormUrlEncoded
    @POST("tmap/routes/pedestrian")
    Call<RouteGuidance> searchRouteGuidanceList(
            @Header("Host") String host,
            @Header("appKey") String appKey,
            @Field("startName") String startName,
            @Field("startX") Number startX,
            @Field("startY") Number startY,
            @Field("endName") String endName,
            @Field("endX") Number endX,
            @Field("endY") Number endY
    );
}
