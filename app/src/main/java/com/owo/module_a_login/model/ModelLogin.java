package com.owo.module_a_login.model;

import java.util.HashMap;

/**
 * Created by XQF on 2017/5/18.
 */
public interface ModelLogin {

    void login(HashMap<String, String> map, OnLoadListener2 listener);

    void loadUser(HashMap<String, String> map, OnLoadListener listener);

}
