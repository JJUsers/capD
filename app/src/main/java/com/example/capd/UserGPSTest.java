package com.example.capd;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;


// 임의 액티비티로 맨 처음 사용자 현재 위치(좌표) 사용하시는 분만 사용하고자 하는 액티비티 내에 코드 붙여넣기 하시면 됩니다
public class UserGPSTest extends AppCompatActivity {

    TextView gps_result;
    Button btn_get_user_location;
    double x,y;
    UserGPS userGPS;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String[] PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_gps_test);

        gps_result = (TextView)findViewById(R.id.gps_result);
        btn_get_user_location = (Button)findViewById(R.id.btn_get_user_location);


        // 사용 예시
        btn_get_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userGPS = new UserGPS(UserGPSTest.this);
                x = userGPS.getLongitude(); //경도
                y = userGPS.getLatitude(); //위도

                gps_result.setText("경도 : " + x + ", " + "위도 : " + y);
            }
        });


        //위치 설정 및 퍼미션 체크
        if (!checkLocationServicesStatus()) {
            LocationServiceSetting();
        }else {
            checkLocationPermission();
        }
    }


    private void LocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserGPSTest.this);
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
    void checkLocationPermission(){

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(UserGPSTest.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(UserGPSTest.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


        } else {  //퍼미션 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserGPSTest.this, PERMISSION[0])) {

                Toast.makeText(UserGPSTest.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(UserGPSTest.this, PERMISSION,
                        PERMISSION_REQUEST_CODE);


            } else {
                ActivityCompat.requestPermissions(UserGPSTest.this, PERMISSION,
                        PERMISSION_REQUEST_CODE);
            }

        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
