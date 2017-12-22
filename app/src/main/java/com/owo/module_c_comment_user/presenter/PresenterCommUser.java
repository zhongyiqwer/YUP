package com.owo.module_c_comment_user.presenter;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/27.
 */
public interface PresenterCommUser {
    void loadLabelSelfBySex(String sex);

    //void loadLabelFindFriend();

    void inuserUserComm(HashMap<String, String> map );
}
