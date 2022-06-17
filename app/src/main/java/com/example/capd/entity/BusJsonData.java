package com.example.capd.entity;

import java.util.ArrayList;

public class BusJsonData {

    public Result RFC30;

    public static class Result {
        public JsonField code;                  // 결과코드
        public JsonField msg;                   // 결과메시지

        public RouteList routeList;             // 노선(버스) array
    }

    public static class RouteList {
        public ArrayList<BusItem> list;         // 노선(버스) array
    }
}
