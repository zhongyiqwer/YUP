package com.owo.module_b_home.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by XQF on 2017/5/20.
 */
public interface ModelHome {

    //加载我已报名的列表
    void loadActivityIApplied(HashMap<String, String> map, OnLoadListener listener);

    //加载我发布的活动列表
    void loadActivityIPublishedAndReceiveApplicatoin(HashMap<String, String> map, OnLoadListener listener);


//    //加载我的道具
//    void loadMyProps(HashMap<String, String> map, OnLoadListener listener);


    /**
     * 加载推荐的活动
     *
     * @param map
     * @param listener
     */
    void loadActivityHomeChangedRecommend(HashMap<String, String> map, OnLoadListener listener);


    /**
     * 加载附近的活动
     *
     * @param map
     * @param listener
     */
    void loadActivityHomeChangedNearby(HashMap<String, String> map, OnLoadListener listener);

    /**
     * 更新用户的hobby
     *
     * @param map
     */

    void sendUserHobby(HashMap<String, String> map);

    /**
     * 最后的对话框要上传的一些数据
     *
     * @param mapList
     */
    void sendUserUpdateInfo(List<HashMap<String, String>> mapList);

//    void loadCam(HashMap<String, String> map, OnLoadListener listener);
//    void loadTxt(HashMap<String, String> map, OnLoadListener listener);


}
