package com.example.capd.entity;

public class StopBusJsonSingleData {

    public Result RFC30;

    public static class Result {
        public JsonField code;                  // 결과코드
        public JsonField msg;                   // 결과메시지

        public RouteList routeList;             // 실시간 노선(버스)
    }

    public static class RouteList {
        public StopBusItem list;                // 실시간 노선(버스)
    }
}
