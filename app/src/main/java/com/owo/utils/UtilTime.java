package com.owo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/1.
 */

public class UtilTime {

    public static HashMap<String,String> getEnterTimeMap(int userID){
        Long currentTime = System.currentTimeMillis();
        String enterTime = currentTime.toString();
        HashMap<String,String> map = new HashMap<>();
        map.put("UserID",""+userID);
        map.put("enterTime",enterTime);
        return map;
    }

    public static HashMap<String,String> getLeaveTimeMap(int userID){
        Long currentTime = System.currentTimeMillis();
        String leaveTime = currentTime.toString();
        HashMap<String,String> map = new HashMap<>();
        map.put("UserID",""+userID);
        map.put("enterTime",leaveTime);
        return map;
    }

    public static String timeFormat(Long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String formatTime = simpleDateFormat.format(date);
        return formatTime;
    }

}
