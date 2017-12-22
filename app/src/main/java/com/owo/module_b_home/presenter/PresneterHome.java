package com.owo.module_b_home.presenter;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/20.
 */
public interface PresneterHome {

    void loadTasksAppliedByUserId(int userId);

    void loadTasksPublishedByUserId(int userId);

    //void loadUserPropNyUserId(int userId);

    void loadTasksRecommendByUserId(int userId);

    void loadTasksNearbyByLatitudeAndLongtitude(String latitude, String longtitude,int userid);

    void sendUserHobbyById(int userId, String hobby);

    void sendUserOtherInfoById(int userId, String avatar, String useranme, int sex, String age);

//    void loadCamByUserId(int userId);
//    void loadTxtByUserId(int userId);

}
