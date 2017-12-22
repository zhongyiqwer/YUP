package com.owo.module_c_detail.detail_activity_comment.view_comment;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ppssyyy on 2017-06-13.
 */
public class ModelActCmtImpl implements  ModelActCmt {
    @Override
    public void loadTaskCmt(final HashMap<String, String> map,final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData =  HttpHelper.postData(MyURL.GET_TASK_COMMENT_BY_TID, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);

    }
}
