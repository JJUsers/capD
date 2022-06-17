package com.example.capd.entity;

public class BusJsonSingleData {

    public Result RFC30;

    public static class Result {
        public JsonField code;                  // 결과코드
        public JsonField msg;                   // 결과메시지

        public RouteList routeList;             // 노선(버스)
    }

    public static class RouteList {
        public BusItem list;                    // 노선(버스)
    }
}
