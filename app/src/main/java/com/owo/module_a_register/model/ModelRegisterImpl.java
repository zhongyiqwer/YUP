package com.owo.module_a_register.model;

import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.owo.utils.util_http.URL;
import com.owo.utils.UtilLog;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author XQF
 * @created 2017/5/18
 */
public class ModelRegisterImpl implements ModelRegister {


    /**
     * 应用注册
     *
     * @param map
     * @param listener
     */

    @Override
    public void register(final HashMap<String, String> map, final OnLoadListener2 listener) {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    String jsonData = HttpHelper.postData(MyURL.INSERT_USER, map, null);
                    UtilLog.d("test", "注册成功返回的数据-------------- jsonData" + jsonData);
                    int code = HttpHelper.getCode(jsonData);
                    listener.onSucess(jsonData,code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 100);
    }


//    /**
//     * pose相机注册
//     *
//     * @param map
//     * @param listener
//     */
//    @Override
//    public void registerPoseCamera(final HashMap<String, String> map,final OnLoadListener2 listener) {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                   String jsonData= HttpHelper.postData(URL.REG_URL, map, null);
//                    UtilLog.d("test", "注册pose成功返回的数据-------------- jsonData" + jsonData);
//                    int code = HttpHelper.getCode(jsonData);
//                    listener.onSucess(jsonData,code);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(task, 100);
//    }


//    /**
//     * pose相机登陆
//     *
//     * @param map
//     * @param
//     */
//    @Override
//    public void loginPoseCamera(final HashMap<String, String> map, final int userId) {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    String jsonResultData = HttpHelper.postData(URL.LOGIN_URL, map, null);
//
//                    UtilLog.d("test", "登录pose相机返回的数据 " + jsonResultData);
//                    HashMap<String, Object> poseID = HttpHelper.AnalysisUid(jsonResultData);
//                    Common.userIdPose = (int) poseID.get("userid"); //获取poseUserID
//                    HashMap<String, String> po = new HashMap<>();
//                    po.put("id", userId + "");
//                    po.put("wexID", Common.userIdPose + "");
//                    HttpHelper.postData(MyURL.UPDATE_WEXID_BY_ID, po, null); //建立poseUserID与yup通道
//
//
//                    HashMap<String, String> param = new HashMap<>();
//                    param.put("userID", userId + "");
//                    param.put("itemID", "10");
//                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, param, null); //注册成功奖励相机道具
//
//                    HashMap<String, String> p = new HashMap<>();
//                    p.put("userID", userId + "");
//                    p.put("itemID", "11");
//                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, p, null); //注册成功奖励纸条道具
//
//                    HashMap<String, String> pose = new HashMap<>();
//                    pose.put("userID", userId + "");
//                    pose.put("itemID", "18");
//                    HttpHelper.postData(MyURL.INSERT_USER_ITEM, pose, null); //注册成功pose相机道具
//
//
//                    HashMap<String, String> data = new HashMap<>();
//                    data.put("userID", Common.userSP.getInt("ID", 0) + "");
//                    data.put("status", "1");
//                    HttpHelper.postData(MyURL.INSERT_STEPS, data, null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.schedule(task, 100);
//    }


}
