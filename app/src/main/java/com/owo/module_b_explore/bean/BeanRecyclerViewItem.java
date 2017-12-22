package com.owo.module_b_explore.bean;

/**
 * @author XQF
 * @created 2017/5/28
 */
public class BeanRecyclerViewItem {

    private int mUserId;
    private String mUrlAvatar;
    private int mSex;
    private double mDis;
    private int weight;
    private int height;
    private String signature;
    private int age;
    private String username;
    private String hobby;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCounter() {
        return mCounter;
    }

    public void setCounter(int counter) {
        mCounter = counter;
    }

    private int mCounter=0;

    public int getSex() {
        return mSex;
    }

    public void setSex(int sex) {
        mSex = sex;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUrlAvatar() {
        return mUrlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        mUrlAvatar = urlAvatar;
    }


    public double getDis() {
        return mDis;
    }

    public void setDis(double dis) {
        mDis = dis;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}
