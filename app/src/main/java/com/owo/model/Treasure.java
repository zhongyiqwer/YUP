package com.owo.model;

/**
 *宝藏
 */
public class Treasure {
    private int id;
    private String latitude;
    private String longtitude;
    private int money;
    private int treasurecode;
    private String userid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTreasurecode() {
        return treasurecode;
    }

    public void setTreasurecode(int treasurecode) {
        this.treasurecode = treasurecode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Treasure{" +
                "id=" + id +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", money=" + money +
                ", treasurecode=" + treasurecode +
                ", userid='" + userid + '\'' +
                '}';
    }
}
