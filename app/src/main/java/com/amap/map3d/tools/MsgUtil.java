package com.amap.map3d.tools;


import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 消息管理工具
 */
public class MsgUtil {

    /**
     * 将指定用户所有消息设为已读
     * @param uid
     * @return
     */
    public static int setAllReadByRID(String uid){
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("recieverID",uid);
            return HttpHelper.getCode(HttpHelper.postData(MyURL.SET_ALL_READ_BY_RID,hm,null));
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 将一条消息设为已读
     * @param mid
     * @return
     */
    public static int setReadByMID(String mid){
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("messageID",mid);
            return HttpHelper.getCode(HttpHelper.postData(MyURL.SET_READ_BY_MID,hm,null));
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 获取用户所有未读消息数
     * @param uid
     * @return
     */
    public static int getUnreadCountByRID(String uid){
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("recieverID",uid);
            String json = HttpHelper.postData(MyURL.GET_UNREAD_COUNT_BY_RID,hm,null);
            int code = HttpHelper.getCode(json);
            if (code==200){
                return (int)HttpHelper.AnalysisData(json);
            } else return -2;

        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }





}
