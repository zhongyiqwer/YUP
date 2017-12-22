package com.owo.module_a_selectlabel.presenter;

/**
 * Created by XQF on 2017/5/27.
 */
public interface PresenterSelectLabel {
    void loadLabelSelfBySex(String sex);

    void loadLabelFindFriend();

    void updateHobbyById(int mUserId, String hobby);
}
