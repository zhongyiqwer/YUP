package com.owo.module_b_addactivity.model;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/26
 */
public class ModelAddAtyImpl implements ModelAddAty {
    private int code;

    @Override
    public int sendTask(final HashMap<String, String> map, final HashMap<String,String > file) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                     code = HttpHelper.getCode(HttpHelper.postData(MyURL.INSERT_TASK, map, file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
        return code;
    }
}
