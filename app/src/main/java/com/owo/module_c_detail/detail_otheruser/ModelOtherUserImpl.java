package com.owo.module_c_detail.detail_otheruser;

import com.owo.module_c_detail.detail_activity.ModelDetail;
import com.owo.module_c_detail.detail_otheruser.OnLoadListener;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ppssyyy on 2017-06-12.
 */
public class ModelOtherUserImpl implements  ModelOtherUser {
    @Override
    public void loadSumIFollowed(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData = HttpHelper.postData(MyURL.GET_SUMIFOLLOWED__BY_FID, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void loadSumFollowMe(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData = HttpHelper.postData(MyURL.GET_SUMFOLLOW_ME__BY_FID, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void loadUserComment(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData = HttpHelper.postData(MyURL.GET_USER_COMMENT_BY_UID, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }
}
