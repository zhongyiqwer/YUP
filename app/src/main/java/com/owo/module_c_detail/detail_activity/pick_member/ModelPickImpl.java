package com.owo.module_c_detail.detail_activity.pick_member;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class ModelPickImpl implements ModelPick {
    String jsonResultData;


    @Override
    public void loadApplyUsers(final HashMap<String, String> map, final OnLoadListener listener) {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    jsonResultData = HttpHelper.postData(MyURL.GET_APPLY_USERS_BY_TID, map, null);
                    HttpHelper.getCode(jsonResultData);

                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);

    }
}
