package com.owo.module_a_login.presenter;

import android.content.Context;

/**
 * Created by XQF on 2017/5/19.
 */
public interface PresenterLogin {
    void loginByPhoneAndPwd(String phone, String pwd);

    void loadUserById(int userId, Context context);
}
