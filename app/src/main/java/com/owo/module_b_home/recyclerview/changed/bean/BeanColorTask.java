package com.owo.module_b_home.recyclerview.changed.bean;

/**
 * @author XQF
 * @created 2017/5/14
 */
public class BeanColorTask extends BeanItemFragHomeChanged implements Comparable<BeanColorTask>{


    int mViewType;
    //标题
    String mTitle;
    //头像
    String mAvatatrUrl;
    //线上活动剩余时间
    String mTime;
    //线上活动发布时间
    String mPubTime;
    //参加人数
    String mSum;
    //距离
    double mDis;
    //性别
    String mSex;
    //昵称
    String mName;
    String image;
    String id;
    String userID;
    private String mTaskLatitude;
    private String mTaskLongitude;
    private int money;
    //最大人数
    String mMax;
    private String taskContent;

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    int locID;
    int status;
    float taskScore;

    private  int taskType;

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getmMax() {
        return mMax;
    }

    public void setmMax(String mMax) {
        this.mMax = mMax;
    }

    public String getmTaskLatitude() {
        return mTaskLatitude;
    }

    public void setmTaskLatitude(String mTaskLatitude) {
        this.mTaskLatitude = mTaskLatitude;
    }

    public String getmTaskLongitude() {
        return mTaskLongitude;
    }

    public void setmTaskLongitude(String mTaskLongitude) {
        this.mTaskLongitude = mTaskLongitude;
    }

    public String getmPubTime() {
        return mPubTime;
    }

    public void setmPubTime(String mPubTime) {
        this.mPubTime = mPubTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public String getAvatatrUrl() {
        return mAvatatrUrl;
    }

    public void setAvatatrUrl(String avatatrUrl) {
        mAvatatrUrl = avatatrUrl;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getSum() {
        return mSum;
    }

    public void setSum(String sum) {
        mSum = sum;
    }

    public double getDis() {
        return mDis;
    }

    public void setDis(double dis) {
        mDis = dis;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getLocID() {
        return locID;
    }

    public void setLocID(int locID) {
        this.locID = locID;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public float getTaskScore() {
        return taskScore;
    }

    public void setTaskScore(float taskScore) {
        this.taskScore = taskScore;
    }

    @Override
    public int compareTo(BeanColorTask o) {
        return o.getLocID()-locID;
    }
}
