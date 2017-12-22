package com.owo.module_a_selectlabel.model;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/27.
 */
public interface ModelSelectLabel {
    void loadLabeSelf(HashMap<String, String> map, OnLoadListener listener);

    void loadLabelFindFriend(OnLoadListener listener);

    void updateHobby(HashMap<String, String> map);
}
