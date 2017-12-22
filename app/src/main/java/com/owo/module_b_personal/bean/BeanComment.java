package com.owo.module_b_personal.bean;


import com.owo.base.BeanBase;
import com.owo.utils.Constants;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class BeanComment extends BeanBase {
    //该条评论的评论者id
    private int mUserId;
    //该评论用户的头像
    private String mUserAvatar;
    //该评论用户ude性别
    private String mUserSex;
    //该回复日期
    private String mReplyDate;
    //回复内容
    private String mReplayContent;

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        mUserAvatar = userAvatar;
    }

    public String getUserSex() {
        return mUserSex;
    }

    public void setUserSex(String userSex) {
        mUserSex = userSex;
    }

    public String getReplyDate() {
        return mReplyDate;
    }

    public void setReplyDate(String replyDate) {
        mReplyDate = replyDate;
    }

    public String getReplayContent() {
        return mReplayContent;
    }

    public void setReplayContent(String replayContent) {
        mReplayContent = replayContent;
    }

    @Override
    public int getBeanType() {
        return Constants.BEAN_TYPE_COMMENT;
    }
}
