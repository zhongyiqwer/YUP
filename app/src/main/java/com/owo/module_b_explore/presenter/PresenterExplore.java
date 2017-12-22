package com.owo.module_b_explore.presenter;

/**
 * Created by XQF on 2017/5/20.
 */
public interface PresenterExplore {
    void getUserOrderByMatchIndex(String userid,String hobby);

    void getUserOrderByDistance(String latitude, String langtitude,int userid);

    void sendAddFriendByIdAndFriendId(int userId, int friendId);
}
