package com.example.capd;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.capd.entity.BusItem;
import com.example.capd.entity.BusJsonData;
import com.example.capd.entity.BusJsonSingleData;
import com.example.capd.entity.StationItem;
import com.example.capd.entity.StationJsonData;
import com.example.capd.entity.StationJsonSingleData;
import com.example.capd.entity.StopBusItem;
import com.example.capd.entity.StopBusJsonData;
import com.example.capd.entity.StopBusJsonSingleData;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class StationSelectActivity extends AppCompatActivity {
    private static final String TAG = "capD";

    private UserGPS userGPS;

        private RequestQueue requestQueue;              // volley requestQueue

    private ProgressDialog progressDialog;              // 로딩 dialog
    private RecyclerView recyclerView1, recyclerView2;
    private Button btnOk;

    private int mode;                                   // 모드 (0:출발, 1:도착)

    private String startId;                             // 출발정류장 id
    private String arrivalId;                           // 도착정류장 id

    private String startName;                           // 출발정류장명
    private String arrivalName;                         // 도착정류장명

    private double startLatitude;                       // 출발정류장 y 좌표 (위도)
    private double startLongitude;                      // 출발정류장 x 좌표 (경도)

    private ArrayList<StationItem> startStations;       // 출발정류장 array
    private ArrayList<StationItem> arrivalStations;     // 도착정류장 array

    private ArrayList<StopBusItem> startStopBusItems;   // 출발정류장 실시간노선 array
    private ArrayList<BusItem> arrivalBusItems;         // 도작정류장 경유노선 array

    final private String[] PERMISSION = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_select_activity);

        // 도착지 위도, 경도 정보
        Intent intent = getIntent();
        final String latitude = intent.getStringExtra("latitude");
        final String longitude = intent.getStringExtra("longitude");

        setTitle(R.string.title_station_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 로딩 dialog
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("처리중...");
        this.progressDialog.setCancelable(false);

        // 출발정류장 리사이클러뷰
        this.recyclerView1 = findViewById(R.id.recyclerView1);
        this.recyclerView1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 도착정류장 리사이클러뷰
        this.recyclerView2 = findViewById(R.id.recyclerView2);
        this.recyclerView2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        this.btnOk = findViewById(R.id.btnOk);
        this.btnOk.setEnabled(false);               // 비활성화

        //위치 설정 및 퍼미션 체크
        if (!checkLocationServicesStatus()) {
            LocationServiceSetting();
        }else {
            checkLocationPermission();
        }

        this.userGPS = new UserGPS(this);

        this.startStations = new ArrayList<>();
        this.arrivalStations = new ArrayList<>();

        this.progressDialog.show();
        // 로딩 dialog 를 표시하기 위해 딜레이를 줌
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // 도착지 먼저 호출하고 출발지 api 호출
            this.mode = 1;

            // 공공데이터 api 호출 (GPS 정보로 정류장목록 조회)
            callOpenApiStation(latitude, longitude);
        }, Constants.LoadingDelay.SHORT);

        findViewById(R.id.btnOk).setOnClickListener(v -> {
            // 확인
            if (checkData()) {
                this.progressDialog.show();

                // 로딩 dialog 를 표시하기 위해 딜레이를 줌
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    // 출발정류장 기준
                    // 공공데이터 api 호출 (정류소별 버스 도착정보 목록 조회)
                    callOpenApi1(this.startId);
                }, Constants.LoadingDelay.SHORT);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.userGPS != null) {
            this.userGPS.stopGPS();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* 정류장 선택 체크 */
    private boolean checkData() {
        boolean selected = false;

        // 출발정류장 선태 체크
        for (StationItem item : this.startStations) {
            if (item.selected) {
                this.startId = item.stopStandardid.content;
                this.startName = item.stopKname.content;

                // 정류장 x 좌표 (경도)
                if (Utils.isNumeric(item.stopx.content)) {
                    this.startLongitude = Double.parseDouble(item.stopx.content);
                } else {
                    this.startLongitude = 0;
                }

                // 정류장 y 좌표 (위도)
                if (Utils.isNumeric(item.stopy.content)) {
                    this.startLatitude = Double.parseDouble(item.stopy.content);
                } else {
                    this.startLatitude = 0;
                }

                selected = true;
                break;
            }
        }

        if (selected) {
            selected = false;

            // 도착정류장 선태 체크
            for (StationItem item : this.arrivalStations) {
                if (item.selected) {
                    this.arrivalId = item.stopStandardid.content;
                    this.arrivalName = item.stopKname.content;
                    selected = true;
                    break;
                }
            }

            if (!selected) {
                Toast.makeText(this, R.string.msg_arrival_station_select_empty, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, R.string.msg_start_station_select_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /* 공공데이터 api 호출 (GPS 정보로 정류장목록 조회) */
    private void callOpenApiStation(final String latitude, final String longitude) {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this);
        }

        Log.d(TAG, "mode:" + this.mode);
        Log.d(TAG, "latitude:" + latitude);
        Log.d(TAG, "longitude:" + longitude);

        try {
            String url = Constants.OpenApi.STATION_API_URL;
            url += "?serviceKey=" + Constants.OpenApi.API_KEY;
            url += "&arg1=" + longitude;                        // X좌표 (경도)
            url += "&arg2=" + latitude;                         // Y좌표 (위도)
            url += "&arg3=5";                                   // 쿼리수

            Log.d(TAG, url);

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        //Log.d(TAG, "result:" + response);

                        // XML 을 JSON 으로 변환
                        XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                        String json = xmlToJson.toString();
                        Log.d(TAG, "json:" + json);

                        // JSON to Object
                        Gson gson = new Gson();

                        // 단일 항목인지 다중 항목인지 체크
                        String[] find = response.split("<list class=");
                        if (find.length > 2) {
                            // 다중
                            StationJsonData data = gson.fromJson(json, StationJsonData.class);

                            //Log.d(TAG, "다중");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                if (data.RFC30.routeList.list != null) {
                                    for (StationItem item : data.RFC30.routeList.list) {
                                        //Log.d(TAG, "id:" + item.stopStandardid.content);
                                        //Log.d(TAG, "name:" + item.stopKname.content);
                                        //Log.d(TAG, "mark:" + item.reMark.content);

                                        if (this.mode == 0) {
                                            // 출발
                                            this.startStations.add(item);
                                        } else {
                                            // 도착
                                            this.arrivalStations.add(item);
                                        }
                                    }
                                }
                            } else {
                                // 실패
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 단일
                            StationJsonSingleData data = gson.fromJson(json, StationJsonSingleData.class);

                            //Log.d(TAG, "단일");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                if (data.RFC30.routeList.list != null) {
                                    if (this.mode == 0) {
                                        // 출발
                                        this.startStations.add(data.RFC30.routeList.list);
                                    } else {
                                        // 도착
                                        this.arrivalStations.add(data.RFC30.routeList.list);
                                    }
                                }
                            } else {
                                // 실패
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (mode == 1) {
                            // 도착
                            if (this.arrivalStations.size() == 0) {
                                // 도착정류장이 없음
                                Toast.makeText(this, R.string.msg_arrival_station_empty, Toast.LENGTH_SHORT).show();
                            } else {
                                // 도착정류장 리스트 구성
                                StationAdapter adapter2 = new StationAdapter(this.arrivalStations);
                                this.recyclerView2.setAdapter(adapter2);

                                this.mode = 0;      // 출발
                                // 공공데이터 api 호출 (출발정류장)
                                callOpenApiStation(String.valueOf(this.userGPS.getLatitude()), String.valueOf(this.userGPS.getLongitude()));
                                //callOpenApiStation("35.814083584749845", "127.08988119743184");
                            }
                        } else {
                            // 출발
                            this.progressDialog.dismiss();

                            if (this.startStations.size() == 0) {
                                // 출발정류장이 없음
                                Toast.makeText(this, R.string.msg_start_station_empty, Toast.LENGTH_SHORT).show();
                            } else {
                                // 출발정류장 리스트 구성
                                StationAdapter adapter1 = new StationAdapter(this.startStations);
                                this.recyclerView1.setAdapter(adapter1);

                                // 확인 버튼 활성화
                                this.btnOk.setEnabled(true);
                            }
                        }
                    },
                    error -> {
                        // 오류
                        onError(error.toString());
                    }
            );

            request.setShouldCache(false);      // 이전 결과가 있어도 새로 요청 (cache 사용 안함)
            this.requestQueue.add(request);
        } catch (Exception e) {
            // 오류
            onError(e.toString());
        }
    }

    /* 공공데이터 api 호출 (정류소별 버스 도착정보 목록 조회) */
    private void callOpenApi1(String stationId) {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this);
        }

        try {
            String url = Constants.OpenApi.STOP_BUS_API_URL;
            url += "?serviceKey=" + Constants.OpenApi.API_KEY;
            url += "&stopStdid=" + URLEncoder.encode(stationId, "UTF-8");   // 정류장 표준 id 로 검색

            Log.d(TAG, url);

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        //Log.d(TAG, "result:" + response);

                        // XML 을 JSON 으로 변환
                        XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                        String json = xmlToJson.toString();
                        Log.d(TAG, "json:" + json);

                        // JSON to Object
                        Gson gson = new Gson();

                        // 단일 항목인지 다중 항목인지 체크
                        String[] find = response.split("<list class=");
                        if (find.length > 2) {
                            // 다중
                            StopBusJsonData data = gson.fromJson(json, StopBusJsonData.class);

                            Log.d(TAG, "다중");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                this.startStopBusItems = new ArrayList<>();

                                if (data.RFC30.routeList.list != null) {
                                    this.startStopBusItems.addAll(data.RFC30.routeList.list);
                                }
                            } else {
                                // 실패
                                this.progressDialog.dismiss();
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            // 단일
                            StopBusJsonSingleData data = gson.fromJson(json, StopBusJsonSingleData.class);

                            Log.d(TAG, "단일");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                this.startStopBusItems = new ArrayList<>();

                                if (data.RFC30.routeList.list != null) {
                                    this.startStopBusItems.add(data.RFC30.routeList.list);
                                }
                            } else {
                                // 실패
                                this.progressDialog.dismiss();
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (this.startStopBusItems.size() > 0) {
                            // 도착정류장의 승강장별 경유노선 목록 조회
                            callOpenApi2(arrivalId);
                        } else {
                            this.progressDialog.dismiss();
                            Toast.makeText(this, R.string.msg_start_station_via_bus_empty, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        // 오류
                        onError(error.toString());
                    }
            );

            request.setShouldCache(false);      // 이전 결과가 있어도 새로 요청 (cache 사용 안함)
            this.requestQueue.add(request);
        } catch (Exception e) {
            // 오류
            onError(e.toString());
        }
    }

    /* 공공데이터 api 호출 (승강장별 경유노선 목록 조회) */
    private void callOpenApi2(String stationId) {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this);
        }

        try {
            String url = Constants.OpenApi.BUS_API_URL;
            url += "?serviceKey=" + Constants.OpenApi.API_KEY;
            url += "&stopStandardid=" + URLEncoder.encode(stationId, "UTF-8");  // 정류장 표준 id 로 검색

            Log.d(TAG, url);

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        //Log.d(TAG, "result:" + response);

                        // XML 을 JSON 으로 변환
                        XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                        String json = xmlToJson.toString();
                        Log.d(TAG, "json:" + json);

                        // JSON to Object
                        Gson gson = new Gson();

                        this.progressDialog.dismiss();

                        // 단일 항목인지 다중 항목인지 체크
                        String[] find = response.split("<list class=");
                        if (find.length > 2) {
                            // 다중
                            BusJsonData data = gson.fromJson(json, BusJsonData.class);

                            Log.d(TAG, "다중");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                this.arrivalBusItems = new ArrayList<>();

                                if (data.RFC30.routeList.list != null) {
                                    this.arrivalBusItems.addAll(data.RFC30.routeList.list);
                                }

                            } else {
                                // 실패
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            // 단일
                            BusJsonSingleData data = gson.fromJson(json, BusJsonSingleData.class);

                            Log.d(TAG, "단일");

                            if (data.RFC30.code.content.equals(Constants.OpenApi.SUCCESS_RESULT_CODE)) {
                                // 성공
                                this.arrivalBusItems = new ArrayList<>();

                                if (data.RFC30.routeList.list != null) {
                                    this.arrivalBusItems.add(data.RFC30.routeList.list);
                                }
                            } else {
                                // 실패
                                Toast.makeText(this, data.RFC30.msg.content, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (this.arrivalBusItems.size() > 0) {
                            // 노선 체크
                            checkBus();
                        } else {
                            Toast.makeText(this, R.string.msg_arrival_station_via_bus_empty, Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        // 오류
                        onError(error.toString());
                    }
            );

            request.setShouldCache(false);      // 이전 결과가 있어도 새로 요청 (cache 사용 안함)
            this.requestQueue.add(request);
        } catch (Exception e) {
            // 오류
            onError(e.toString());
        }
    }

    /* 오류 확인 */
    private void onError(String error) {
        Log.d(TAG, "error:" + error);
        this.progressDialog.dismiss();
        Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show();
    }

    /* 노선 확인 */
    private void checkBus() {
        GlobalVariable.findStopBusItems = new ArrayList<>();

        // 도착지를 경유하는 노선 찾기
        for (BusItem item : this.arrivalBusItems) {
            /*
            Log.d(TAG, "노선 id:" + item.brtStdId.content);
            Log.d(TAG, "노선번호:" + item.brtId.content);
            Log.d(TAG, "노선확장:" + item.brtClass.content);
            Log.d(TAG, "기점명:" + item.brtSname.content);
            Log.d(TAG, "종점명:" + item.brtEname.content);
            */

            for (StopBusItem stopItem : this.startStopBusItems) {
                if (item.brtStdId.content.equals(stopItem.brtStdid.content)) {
                    Log.d(TAG, "find 노선 id:" + stopItem.brtStdid.content);

                    GlobalVariable.findStopBusItems.add(stopItem);
                    break;
                }
            }
        }

        if (GlobalVariable.findStopBusItems.size() == 0) {
            Toast.makeText(this, R.string.msg_arrival_station_via_bus_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 버스목록 activity 호출
        Intent intent = new Intent(this, BusListActivity.class);
        intent.putExtra("start_station", this.startName);
        intent.putExtra("arrival_station", this.arrivalName);
        intent.putExtra("start_latitude", this.startLatitude);          // 출발정류장 y 좌표 (위도)
        intent.putExtra("start_longitude", this.startLongitude);        // 출발정류장 x 좌표 (경도)
        startActivity(intent);

        finish();
    }


    // 위치 서비스 활성화 작업
    private void LocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        } else {  //퍼미션 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION[0])) {

                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
