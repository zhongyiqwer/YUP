package com.owo.model;

/**
 * 照相机记录
 */
public class PhotoRecord {
    private int id;
    private String avatar;
    private String latitude;
    private String longtitude;
    private String publishDate;
    private String userName;
    private String photo;
    private String content;


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "PhotoRecord{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", userName='" + userName + '\'' +
                ", photo='" + photo + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
