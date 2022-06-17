package com.example.capd;

public class Constants {

    /* 공공데이터 open api 관련 데이터 */
    public static class OpenApi {
        public static final String API_KEY = "AwRbaT3aKDn7OkKd7kOGQ27Xex7Vk2MEGe40P0nIasIML3R28w15roIg2zY5vALWp98E3VDQYZBdr9sjZRSSCQ%3D%3D";    // api key
        public static final String STATION_API_URL = "http://openapi.jeonju.go.kr/jeonjubus/openApi/traffic/bus_stop_approach_common.do";       // GPS 정보로 승강장명목록 조회
        public static final String STOP_BUS_API_URL = "http://openapi.jeonju.go.kr/jeonjubus/openApi/traffic/bus_location_busstop_infomation_common.do"; // 정류소별 버스 도착정보 목록 조회
        public static final String BUS_API_URL = "http://openapi.jeonju.go.kr/jeonjubus/openApi/traffic/general_busstop_common.do";             // 승강장별 경유노선 목록 조회
        public static final String SUCCESS_RESULT_CODE = "000";                         // open api 성공 result code
    }

    /* 로딩 딜레이 */
    public static class LoadingDelay {
        public static final int SHORT = 500;
        public static final int LONG = 1000;
    }
}
