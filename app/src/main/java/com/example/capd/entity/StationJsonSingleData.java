package com.example.capd.entity;

public class StationJsonSingleData {

    public Result RFC30;

    public static class Result {
        public JsonField code;                  // 결과코드
        public JsonField msg;                   // 결과메시지

        public RouteList routeList;             // 정류장
    }

    public static class RouteList {
        public StationItem list;                // 정류장
    }
}
