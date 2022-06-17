package com.example.capd;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.capd.Navigation.PostNavigationService;
import com.example.capd.Navigation.RouteGuidance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity {

    Button btn_voice_guide;
    Button btn_arrive;
    TextView direction1;
    TextView direction2;
    String startName, endName;
    Number start_x, start_y, end_x, end_y; //x : 경도 , y : 위도
    String appKey, host;
    UserGPS userGPS;
    TTS tts;


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String[] PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        btn_voice_guide = (Button)findViewById(R.id.btn_voice_guide);
        btn_arrive = (Button)findViewById(R.id.btn_arrive);
        direction1 = (TextView)findViewById(R.id.direction1);
        direction2 = (TextView)findViewById(R.id.direction2);

        startName  = "출발지";
        endName = "목적지"; // 정류장 이름

        end_x = 126.92577620;
        end_y = 37.55337145;

        host = "apis.openapi.sk.com";
        appKey = "l7xxb504986a552948ffa554311c56469bc7";

        tts = new TTS(this);


        /* Intent intent = new Intent(this.getIntent());
        end_x = intent.getStringExtra("coordinatesX");
        end_y = intent.getStringExtra("coordinatesY");
        endName = intent.getStringExtra("busStopName"); */


        if (!checkLocationServicesStatus()) {
            LocationServiceSetting();
        }else {
            checkLocationPermission();
        }


        userGPS = new UserGPS(NavigationActivity.this);


        btn_voice_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://apis.openapi.sk.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PostNavigationService service = retrofit.create(PostNavigationService.class);

                start_x = userGPS.getLongitude();
                start_y = userGPS.getLatitude();

                service.searchRouteGuidanceList(host, appKey, startName, start_x, start_y, endName, end_x, end_y).enqueue(new Callback<RouteGuidance>() {
                    @Override
                    public void onResponse(Call<RouteGuidance> call, Response<RouteGuidance> response) {
                        if (response.isSuccessful()){

                            RouteGuidance routeGuidance = response.body();

                            String direction1_description = routeGuidance.features.get(0).properties.getDescription();
                            String direction2_description = routeGuidance.features.get(2).properties.getDescription();

                            direction1.setText(direction1_description);
                            direction2.setText(direction2_description);

                            if (start_x != end_x || start_y != end_y) {
                                tts.startTTS(direction1_description + " . " + direction2_description);

                            }else {
                                tts.startTTS("목적지에 도착하였습니다.");
                            }

                        }else
                        {
                            Toast.makeText(NavigationActivity.this, "값 불러오기 실패", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<RouteGuidance> call, Throwable t) {
                        Log.d("post", t.getMessage());
                    }
                });
            }
        });



        btn_arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                userGPS.stopGPS();
            }
        });


        Button.OnLongClickListener onLongClickListener = new Button.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                switch(v.getId()){
                    case R.id.btn_voice_guide :
                        tts.startTTS(btn_voice_guide.getText().toString());
                        break;
                    case R.id.btn_arrive :
                        tts.startTTS(btn_arrive.getText().toString());
                        break;
                }
                return true;
            }
        };

        btn_voice_guide.setOnLongClickListener(onLongClickListener);
        btn_arrive.setOnLongClickListener(onLongClickListener);

    }


    // 위치 서비스 활성화 작업
    private void LocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치를 설정해주세요");
        builder.setCancelable(true);

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }



    //퍼미션 체크
    public void checkLocationPermission(){

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(NavigationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(NavigationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


        } else {  //퍼미션 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(NavigationActivity.this, PERMISSION[0])) {

                Toast.makeText(NavigationActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(NavigationActivity.this, PERMISSION,
                        PERMISSION_REQUEST_CODE);


            } else {
                ActivityCompat.requestPermissions(NavigationActivity.this, PERMISSION,
                        PERMISSION_REQUEST_CODE);
            }

        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onDestroy() {
        tts.close();
        super.onDestroy();
    }
}





