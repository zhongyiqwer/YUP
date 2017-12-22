package com.owo.module_b_home.model;

import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/20
 */
public class ModelHomeImpl implements ModelHome {

//    @Override
//    public  void loadCam(final HashMap<String, String> map, final OnLoadListener listener) {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    String jsonResultData = HttpHelper.postData(MyURL.GET_PHOTOS_EXCEPT_ONE, map, null);
//                    listener.onSucess(jsonResultData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(task, 100);
//    }
//
//    @Override
//    public  void loadTxt(final HashMap<String, String> map, final OnLoadListener listener) {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    String jsonResultData = HttpHelper.postData(MyURL.GET_TEXTS_EXCEPT_ONE, map, null);
//                    listener.onSucess(jsonResultData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(task, 100);
//    }



    @Override
    public void loadActivityIApplied(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String jsonResultData = null;
                try {
                     jsonResultData = HttpHelper.postData(MyURL.GET_TASK_I_APPLIED, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onSucess(jsonResultData);
                }
            }
        };
        timer.schedule(task, 100);
    }

    @Override
    public void loadActivityIPublishedAndReceiveApplicatoin(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String jsonResultData = null;
                try {
                     jsonResultData = HttpHelper.postData(MyURL.GET_TASK_I_PUBLISHED, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onSucess(jsonResultData);
                }
            }
        };
        timer.schedule(task, 100);
    }


//    @Override
//    public void loadMyProps(final HashMap<String, String> map, final OnLoadListener listener) {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    String jsonResultData = HttpHelper.postData(MyURL.GET_ITEMS_BY_UID, map, null);
//                    listener.onSucess(jsonResultData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(task, 100);
//    }

    @Override
    public void loadActivityHomeChangedRecommend(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData = HttpHelper.postData(MyURL.GET_TASK_RECOMEND, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 500);
    }

    @Override
    public void loadActivityHomeChangedNearby(final HashMap<String, String> map, final OnLoadListener listener) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonResultData = HttpHelper.postData(MyURL.GET_TASK_ORDER_BY_DIS, map, null);
                    listener.onSucess(jsonResultData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 500);
    }


    //上传hobby
    @Override
    public void sendUserHobby(final HashMap<String, String> map) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {

                    //虽然有返回数据，暂时不处理，。，。，。
                    //上传hobby
                    String jsonResultData = HttpHelper.postData(MyURL.UPDATE_HOBBY_BY_ID, map, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }


    /**
     * 上传后面的对话框中的数据
     * 0 是userId  map ,
     * 1 是头像的文件map,
     * 2 是用户名map
     * 3 是用户性别map
     * 4 是用户年龄map
     *
     * @param mapList
     */

    @Override
    public void sendUserUpdateInfo(final List<HashMap<String, String>> mapList) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {

                    //虽然有返回数据，暂时不处理，。，。，。
                    //上传头像
                    if (mapList.get(1).get("avatar") != null) {

                        HttpHelper.postData(MyURL.UPDATE_AVATAR_BY_ID, mapList.get(0), mapList.get(1));
                    }
                    //上传用户名
                    HttpHelper.postData(MyURL.UPDATE_USERNAME_BY_ID, mapList.get(2), null);
                    //上传性别
                    HttpHelper.postData(MyURL.UPDATE_SEX_BY_ID, mapList.get(3), null);
                    //上传年龄
                    HttpHelper.postData(MyURL.UPDAET_AGE_BY_ID, mapList.get(4), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }


}
