package com.owo.model;


/**
 * 动态
 */
public class Moment  implements Comparable<Moment> {
    private int id;
    private int momentType;
    private String momentContent;
    private String momentImage;
    private String momentDate;
    private int userID;
    private String userAvatar;
    private String userName;

    public int getMomentType() {
        return momentType;
    }

    public void setMomentType(int momentType) {
        this.momentType = momentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMomentContent() {
        return momentContent;
    }

    public void setMomentContent(String momentContent) {
        this.momentContent = momentContent;
    }

    public String getMomentImage() {
        return momentImage;
    }

    public void setMomentImage(String momentImage) {
        this.momentImage = momentImage;
    }

    public String getMomentDate() {
        return momentDate;
    }

    public void setMomentDate(String momentDate) {
        this.momentDate = momentDate;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Moment{" +
                "id=" + id +
                ", momentType=" + momentType +
                ", momentContent='" + momentContent + '\'' +
                ", momentImage='" + momentImage + '\'' +
                ", momentDate='" + momentDate + '\'' +
                ", userID=" + userID +
                ", userAvatar='" + userAvatar + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Moment moment) {
        return (int)((Long.parseLong(moment.getMomentDate())-Long.parseLong(this.momentDate))/1000);
    }
}
