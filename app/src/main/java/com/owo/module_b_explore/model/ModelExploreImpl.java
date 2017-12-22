package com.owo.module_b_explore.model;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/26
 */
public class ModelExploreImpl implements ModelExplore {
    @Override
    public void loadRecommendUser(final HashMap<String, String> map,
                                  final HashMap<String,String > file,final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {

                    String jsonData = HttpHelper.postData(MyURL.GET_USER_ORDER_BY_MATCHINDEX, map, file);

                    listener.onSucess(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 500);
    }

    @Override
    public void loadUserByDistance(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonData = HttpHelper.postData(MyURL.GET_USER_ORDER_BY_DIS, map, null);
                    listener.onSucess(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 400);
    }

    @Override
    public void sendAddFriend(final HashMap<String, String> map) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpHelper.postData(MyURL.INSERT_FRIEND, map, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }
}
