package com.owo.module_a_login.view;

import com.owo.module_b_personal.bean.BeanUser;

/**
 * Created by XQF on 2017/5/20.
 */
public interface ViewLogin {
    void getResultLogin(String string,int code);

    void getResultGetUserById(BeanUser beanUser);
}
