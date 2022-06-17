package com.example.capd.Navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("intersectionName")
    @Expose
    private String intersectionName;
    @SerializedName("nearPoiName")
    @Expose
    private String nearPoiName;
    @SerializedName("nearPoiX")
    @Expose
    private String nearPoiX;
    @SerializedName("nearPoiY")
    @Expose
    private String nearPoiY;
    @SerializedName("turnType")
    @Expose
    private Number turnType;
    @SerializedName("pointType")
    @Expose
    private String pointType;
    @SerializedName("distance")
    @Expose
    private Number distance;
    @SerializedName("time")
    @Expose
    private Number time;
    @SerializedName("facilityType")
    @Expose
    private String facilityType;
    @SerializedName("facilityName")
    @Expose
    private String facilityName;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    public void setIntersectionName(String intersectionName) {
        this.intersectionName = intersectionName;
    }

    public String getNearPoiName() {
        return nearPoiName;
    }

    public void setNearPoiName(String nearPoiName) {
        this.nearPoiName = nearPoiName;
    }

    public String getNearPoiX() {
        return nearPoiX;
    }

    public void setNearPoiX(String nearPoiX) {
        this.nearPoiX = nearPoiX;
    }

    public String getNearPoiY() {
        return nearPoiY;
    }

    public void setNearPoiY(String nearPoiY) {
        this.nearPoiY = nearPoiY;
    }

    public Number getTurnType() {
        return turnType;
    }

    public void setTurnType(Number turnType) {
        this.turnType = turnType;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public Number getDistance() {
        return distance;
    }

    public void setDistance(Number distance) {
        this.distance = distance;
    }

    public Number getTime() {
        return time;
    }

    public void setTime(Number time) {
        this.time = time;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
}
