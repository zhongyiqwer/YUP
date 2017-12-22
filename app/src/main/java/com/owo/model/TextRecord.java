package com.owo.model;

/**
 * 小纸条记录
 */
public class TextRecord {
    private int id;
    private String avatar;
    private String latitude;
    private String longtitude;
    private String publishDate;
    private String userName;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "TextRecord{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", userName='" + userName + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
