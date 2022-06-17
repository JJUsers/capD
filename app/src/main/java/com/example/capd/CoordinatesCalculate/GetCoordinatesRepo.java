package com.example.capd.CoordinatesCalculate;

import android.util.Log;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCoordinatesRepo {

    private static GetCoordinatesRepo instance;
    private String kakaoApi = "8e73f13b9237b659859da56c25dfc1a3";

    public static GetCoordinatesRepo getINSTANCE(){
        if(instance == null){
            instance = new GetCoordinatesRepo();
        }

        return instance;
    }

    public void getCoordinatesList(String query, int page, int size, final CoordinatesResponseListener listener){
        if (query != null){
            Call<Coordinates> call = RetrofitFactory
                    .getRetrofit()
                    .getCoordinatesService()
                    .searchCoordinatesList(query, page, size, "KakaoAK " + kakaoApi);
            call.enqueue(new Callback<Coordinates>() {
                @Override
                public void onResponse(Call<Coordinates> call, Response<Coordinates> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            for (int i=0; i<response.body().documentsList.size(); i++){
                                Log.i("좌표","[GET] getCoordinatesList : " + response.body().documentsList.get(i).getPlace_name());
                            }
                            listener.onSuccessResponse(response.body());

                        }

                    }
                }

                @Override
                public void onFailure(Call<Coordinates> call, Throwable t) {
                    listener.onFailResponse();

                }
            });
        }
    }
    public interface CoordinatesResponseListener{
        void onSuccessResponse(Coordinates coordinatesData);
        void onFailResponse();
    }

}
