package com.owo.module_b_personal.bean;


import com.owo.base.BeanBase;
import com.owo.utils.Constants;

/**
 * @author XQF
 * @created 2017/5/23
 */
public class BeanLike extends BeanBase {
    //点赞人的id
    private int mUserId;
    //点赞人的头像
    private String mUserAvatar;

    public String getLikeDate() {
        return mLikeDate;
    }

    public void setLikeDate(String likeDate) {
        mLikeDate = likeDate;
    }

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

    //点赞时间
    private String mLikeDate;

    @Override
    public int getBeanType() {
        return Constants.BEAN_TYPE_LIKE;
    }
}
