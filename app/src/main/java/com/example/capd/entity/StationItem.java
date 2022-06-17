package com.example.capd.entity;

public class StationItem {

    public JsonField stopKname;             // 정류장명
    public JsonField stopStandardid;        // 정류장 표준 id
    public JsonField stopId;                // 정류장 서비스 id
    public JsonField reMark;                // 참고사항

    public JsonField stopx;                 // 정류장 x 좌표 (경도)
    public JsonField stopy;                 // 정류장 y 좌표 (위도)

    public boolean selected;                // 선택 여부

}
