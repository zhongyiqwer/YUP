package com.owo.module_c_comment_user.model;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/27.
 */
public interface ModelCommUser {
    void loadLabeSelf(HashMap<String, String> map, OnLoadListener listener);

    //void loadLabelFindFriend(OnLoadListener listener);

    void insertUserComment(HashMap<String, String> map);
}
