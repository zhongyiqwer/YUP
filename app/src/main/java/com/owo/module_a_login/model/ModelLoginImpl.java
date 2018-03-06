package com.owo.module_a_login.model;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/22
 */
public class ModelLoginImpl implements ModelLogin {
    @Override
    public void login(final HashMap<String, String> map, final OnLoadListener2 listener) {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String result = HttpHelper.postData(MyURL.GET_ID_BY_PHONE_AND_PW, map, null);
                    int code = HttpHelper.getCode(result);
                    listener.onSucess(result,code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void loadUser(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String result = HttpHelper.postData(MyURL.GET_USER_BY_ID, map, null);
                    listener.onSucess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }
}
