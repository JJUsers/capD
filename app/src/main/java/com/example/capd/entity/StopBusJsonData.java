package com.example.capd.entity;

import java.util.ArrayList;

public class StopBusJsonData {

    public Result RFC30;

    public static class Result {
        public JsonField code;                  // 결과코드
        public JsonField msg;                   // 결과메시지

        public RouteList routeList;             // 실시간 노선(버스) array
    }

    public static class RouteList {
        public ArrayList<StopBusItem> list;     // 실시간 노선(버스) array
    }
}
