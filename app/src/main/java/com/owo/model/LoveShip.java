package com.owo.model;

/**
 * 恋爱关系
 */
public class LoveShip {

    private int loverAID;
    private int loverBID;
    private String avatarA;
    private String avatarB;
    private String nameA;
    private String nameB;
    private String loveIndex;
    private String loverID;
    private String checkinDays;
    private double longtitude;
    private double latitude;
    private String signature;
    private String loveTime;
    private int state;

    public int getLoverAID() {
        return loverAID;
    }

    public void setLoverAID(int loverAID) {
        this.loverAID = loverAID;
    }

    public int getLoverBID() {
        return loverBID;
    }

    public void setLoverBID(int loverBID) {
        this.loverBID = loverBID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLoverID() {
        return loverID;
    }

    public void setLoverID(String loverID) {
        this.loverID = loverID;
    }

    public String getLoveTime() {
        return loveTime;
    }

    public void setLoveTime(String loveTime) {
        this.loveTime = loveTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public String getAvatarA() {
        return avatarA;
    }

    public void setAvatarA(String avatarA) {
        this.avatarA = avatarA;
    }

    public String getAvatarB() {
        return avatarB;
    }

    public void setAvatarB(String avatarB) {
        this.avatarB = avatarB;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getLoveIndex() {
        return loveIndex;
    }

    public void setLoveIndex(String loveIndex) {
        this.loveIndex = loveIndex;
    }

    public String getCheckinDays() {
        return checkinDays;
    }

    public void setCheckinDays(String checkinDays) {
        this.checkinDays = checkinDays;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "LoveShip{" +
                "loverAID=" + loverAID +
                ", loverBID=" + loverBID +
                ", avatarA='" + avatarA + '\'' +
                ", avatarB='" + avatarB + '\'' +
                ", nameA='" + nameA + '\'' +
                ", nameB='" + nameB + '\'' +
                ", loveIndex='" + loveIndex + '\'' +
                ", loverID='" + loverID + '\'' +
                ", checkinDays='" + checkinDays + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", signature='" + signature + '\'' +
                ", loveTime='" + loveTime + '\'' +
                ", state=" + state +
                '}';
    }
}
