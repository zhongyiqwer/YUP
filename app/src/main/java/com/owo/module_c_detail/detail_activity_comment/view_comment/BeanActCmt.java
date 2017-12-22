package com.owo.module_c_detail.detail_activity_comment.view_comment;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class BeanActCmt {
    private String avator;
    private String sex;
    private String userName;
    private String commentdate;
    private int id;
    private String taskcomment;
    private int taskid;
    private float taskscore;
    private int userid;

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public void setCommentdate(String commentdate) {
        this.commentdate = commentdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskcomment() {
        return taskcomment;
    }

    public void setTaskcomment(String taskcomment) {
        this.taskcomment = taskcomment;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public float getTaskscore() {
        return taskscore;
    }

    public void setTaskscore(float taskscore) {
        this.taskscore = taskscore;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "BeanActCmt{" +
                "avator='" + avator + '\'' +
                ", sex='" + sex + '\'' +
                ", userName='" + userName + '\'' +
                ", commentdate='" + commentdate + '\'' +
                ", id=" + id +
                ", taskcomment='" + taskcomment + '\'' +
                ", taskid=" + taskid +
                ", taskscore=" + taskscore +
                ", userid=" + userid +
                '}';
    }
}
