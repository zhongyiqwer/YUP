package com.owo.module_c_detail.detail_otheruser;

import com.owo.module_c_detail.detail_otheruser.OnLoadListener;

import java.util.HashMap;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public interface ModelOtherUser {
    void loadUserComment(HashMap<String, String> map, OnLoadListener listener);

    void loadSumIFollowed(HashMap<String, String> map, OnLoadListener listener);

    void loadSumFollowMe(HashMap<String, String> map, OnLoadListener listener);

}
