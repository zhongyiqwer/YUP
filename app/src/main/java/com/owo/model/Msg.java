package com.owo.model;



/**
 * 消息
 */
public class Msg implements Comparable<Msg>{
    private int msgId;
    private String msgContent;
    private String msgDate;
    private String userAvatar;
    private String userName;
    private String msgType;
    private int userID;
    private int isRead;


    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msgId=" + msgId +
                ", msgContent='" + msgContent + '\'' +
                ", msgDate='" + msgDate + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userName='" + userName + '\'' +
                ", msgType='" + msgType + '\'' +
                ", userID=" + userID +
                ", isRead=" + isRead +
                '}';
    }

    @Override
    public int compareTo(Msg msg) {
        return (int)((Long.parseLong(msg.getMsgDate())-Long.parseLong(this.msgDate))/1000);
    }
}
