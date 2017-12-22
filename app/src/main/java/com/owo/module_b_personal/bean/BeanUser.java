package com.owo.module_b_personal.bean;

import java.io.Serializable;

/**
 * @author XQF
 * @created 2017/5/20
 */
public class BeanUser implements Serializable {
    private int id;
    private String userName;
    private String passWord;
    private String avatar;
    private String wexID;
    private String qq;
    private String phoneNumber;
    private int level;
    private String longtitude;
    private String latitude;
    private String inviteCode;
    private String sex;
    private int stepsToday;
    private String signature;
    private int status;
    private int checkinDays;
    private int age;
    private int height;
    private int weight;
    private String hobby;
    private int exp;
    private String backImage;
    private int money;
    private String regDate;


//    //单例
//    private static BeanUser mBeanUser = null;
//
//    private BeanUser() {
//
//    }
//
//    public static BeanUser newInstance() {
//        if (mBeanUser == null) {
//            mBeanUser = new BeanUser();
//        }
//        return mBeanUser;
//    }


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCheckinDays() {
        return checkinDays;
    }

    public void setCheckinDays(int checkinDays) {
        this.checkinDays = checkinDays;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWexID() {
        return wexID;
    }

    public void setWexID(String wexID) {
        this.wexID = wexID;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getStepsToday() {
        return stepsToday;
    }

    public void setStepsToday(int stepsToday) {
        this.stepsToday = stepsToday;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", avatar='" + avatar + '\'' +
                ", wexID='" + wexID + '\'' +
                ", qq='" + qq + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", level=" + level +
                ", longtitude='" + longtitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", sex='" + sex + '\'' +
                ", stepsToday=" + stepsToday +
                ", signature='" + signature + '\'' +
                ", status=" + status +
                ", checkinDays=" + checkinDays +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", hobby='" + hobby + '\'' +
                ", exp=" + exp +
                ", backImage='" + backImage + '\'' +
                ", money=" + money +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
