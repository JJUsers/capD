package com.example.capd.CoordinatesCalculate;

import com.google.gson.annotations.SerializedName;

public class Data{

    @SerializedName("place_name")
    private String place_name;
    @SerializedName("x") // 위도
    private String x;
    @SerializedName("y") // 경도
    private String y;

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}

