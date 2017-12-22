package com.owo.module_a_selectlabel.model;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/27
 */
public class ModelSelectLabelImpl implements ModelSelectLabel {


    @Override
    public void loadLabeSelf(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonData = HttpHelper.postData(MyURL.GET_INTERESTTAGBYSEX, map, null);
                    listener.onSucess(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void loadLabelFindFriend(final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonData = HttpHelper.postData(MyURL.GET_INTERESTTAG, null, null);
                    listener.onSucess(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void updateHobby(final HashMap<String, String> map) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpHelper.postData(MyURL.UPDATE_HOBBY_BY_ID, map, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }


}
