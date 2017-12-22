package com.owo.module_b_personal.model;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/20.
 */
public interface ModelPersonal {

    //加载用户个人信息
    void loadUserPersonalInfo(HashMap<String, String> map, OnLoadListener listener);

    //加载用户朋友们的动态
    void loadPersoalFriendMoment(HashMap<String, String> map, OnLoadListener2 listener);


    //加载我已报名的列表
   // void loadActivityIApplied(HashMap<String, String> map, OnLoadListener2 listener);

    //加载我发布的活动列表
    void loadActivityIPublishedAndReceiveApplicatoin(HashMap<String, String> map, OnLoadListener2 listener);


    //加载动态详情，包括评论
    void loadPersonalDetailMoment(HashMap<String, String> map, OnLoadListener listener);

    //加载个人主页的活动详情
    void loadPersonalDetailActivity(HashMap<String, String> map, OnLoadListener listener);

    void loadSumIFollowed(HashMap<String, String> map, OnLoadListener listener);

    void loadSumFollowMe(HashMap<String, String> map, OnLoadListener listener);

    void loadResultComment(HashMap<String, String> map, OnLoadListener listener);

    void loadResultLike(HashMap<String, String> map, OnLoadListener listener);

    void sendUpdateLikeSum(HashMap<String, String> map);

    void loadUserComment(HashMap<String, String> map, OnLoadListener listener);
}
