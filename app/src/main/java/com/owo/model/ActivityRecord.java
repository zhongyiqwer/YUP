package com.owo.model;

/**
 * 情侣圈
 */
public class ActivityRecord {
    private int id;
    private String loverID;
    private int radius;
    private  int totalSteps;
    private String longtitude;
    private String latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoverID() {
        return loverID;
    }

    public void setLoverID(String loverID) {
        this.loverID = loverID;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ActivityRecord{" +
                "id=" + id +
                ", loverID='" + loverID + '\'' +
                ", radius=" + radius +
                ", totalSteps=" + totalSteps +
                ", longtitude='" + longtitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
