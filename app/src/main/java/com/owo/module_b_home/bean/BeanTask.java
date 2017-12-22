package com.owo.module_b_home.bean;

import com.owo.base.BeanBase;
import com.owo.utils.Constants;

import java.io.Serializable;

/**
 * @author XQF
 * @created 2017/5/20
 */
//任务
public class BeanTask extends BeanBase implements Serializable {

    private int mTaskId;
    private int mTaskUserID;//任务发起人ID
    private int mTaskType;//任务类型
    private String mTaskName;//任务名称
    private String mTaskIamge;//任务图片
    private String mTaskContent;//任务内容
    private String mTaskDeadLine;//任务截至时间
    private String mTaskPublishTime;//任务发布时间
    private String mTaskLatitude;//维度
    private String mTaskLongitude;//经度
    private int mTaskTakenNum;//实际参与人数
    private int mTaskMaxNum;//最大参与人数
    private String mTaskLabel;//任务标签（吃，玩，线上）
    private int mTaskMoney;//每个参与者要支付的钱
    private int mTaskStatus;//任务状态
    private float mTaskScore;//任务总体评分
    private float mAvgScore;//任务算数平均分'

    private String avatar;
    private int sex;
    private String userName;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }



    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int taskId) {
        mTaskId = taskId;
    }

    public int getTaskUserID() {
        return mTaskUserID;
    }

    public void setTaskUserID(int taskUserID) {
        mTaskUserID = taskUserID;
    }

    public int getTaskType() {
        return mTaskType;
    }

    public void setTaskType(int taskType) {
        mTaskType = taskType;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public String getTaskIamge() {
        return mTaskIamge;
    }

    public void setTaskIamge(String taskIamge) {
        mTaskIamge = taskIamge;
    }

    public String getTaskContent() {
        return mTaskContent;
    }

    public void setTaskContent(String taskContent) {
        mTaskContent = taskContent;
    }

    public String getTaskDeadLine() {
        return mTaskDeadLine;
    }

    public void setTaskDeadLine(String taskDeadLine) {
        mTaskDeadLine = taskDeadLine;
    }

    public String getTaskPublishTime() {
        return mTaskPublishTime;
    }

    public void setTaskPublishTime(String taskPublishTime) {
        mTaskPublishTime = taskPublishTime;
    }

    public String getTaskLatitude() {
        return mTaskLatitude;
    }

    public void setTaskLatitude(String taskLatitude) {
        mTaskLatitude = taskLatitude;
    }

    public String getTaskLongitude() {
        return mTaskLongitude;
    }

    public void setTaskLongitude(String taskLongitude) {
        mTaskLongitude = taskLongitude;
    }

    public int getTaskTakenNum() {
        return mTaskTakenNum;
    }

    public void setTaskTakenNum(int taskTakenNum) {
        mTaskTakenNum = taskTakenNum;
    }

    public int getTaskMaxNum() {
        return mTaskMaxNum;
    }

    public void setTaskMaxNum(int taskMaxNum) {
        mTaskMaxNum = taskMaxNum;
    }

    public String getTaskLabel() {
        return mTaskLabel;
    }

    public void setTaskLabel(String taskLabel) {
        mTaskLabel = taskLabel;
    }

    public int getTaskMoney() {
        return mTaskMoney;
    }

    public void setTaskMoney(int taskMoney) {
        mTaskMoney = taskMoney;
    }

    public int getTaskStatus() {
        return mTaskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        mTaskStatus = taskStatus;
    }

    public float getTaskScore() {
        return mTaskScore;
    }

    public void setTaskScore(float taskScore) {
        mTaskScore = taskScore;
    }

    public float getAvgScore() {
        return mAvgScore;
    }

    public void setAvgScore(float avgScore) {
        mAvgScore = avgScore;
    }

    @Override
    public int getBeanType() {
        return Constants.BEAN_TYPE_TASK;
    }
}
