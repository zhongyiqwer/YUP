package com.wao.dogcat.controller.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


import com.amap.map3d.tools.ExpUtil;
import com.owo.module_b_personal.widgets.FragPersonalViewpagerRight;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.service.AlarmReceiver;

/**
 * Created by ppssyyy on 2017-03-22.
 */
public class TimeService extends Service {
    private boolean flag = false;
    private int realSteps;
    public static int level;

    @Override
    public void onCreate() {
        System.out.println("TimeService:onCreate");

        ////////////////////////////////
    }

    public void updateData() {
        realSteps = Pedometer.mCount/10;
        if (Common.userID != -1 && Common.user != null) {

            //System.out.println("步数！！！！！！！"+Pedometer.mCount);

            //UPDATE_STEPS
            try {
                HashMap<String, String> param = new HashMap<>();
                param.put("userID", Common.userID + "");
                SimpleDateFormat formatter  =
                        new SimpleDateFormat ("yyyy-MM-dd");
                Date  curDate  =  new  Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                param.put("date",str);
                param.put("steps", Pedometer.mCount + "");
                String s = HttpHelper.postData(MyURL.UPDATE_STEPS, param, null);

                //System.out.println("post步数！！！！！！！"+s);


                //UPDATE_EXP
                int addExp = ExpUtil.stepsToExp(Pedometer.mCount, 0, 1);
                if (addExp != -1) {
                    level = ExpUtil.getLevelByAddExp(addExp, Common.user.getExp(), Common.user.getLevel());

                    //更新当前总经验
                    HashMap<String, String> pp = new HashMap<>();
                    pp.put("id", Common.userID + "");
                    pp.put("exp", addExp + Common.user.getExp() + "");
                    HttpHelper.postData(MyURL.UPDATE_EXP_BY_ID, pp, null);
                    Common.user.setExp(addExp + Common.user.getExp());

                    //更新当前等级
                    HashMap<String, String> qq = new HashMap<>();
                    qq.put("id", Common.userID + "");
                    qq.put("level", level + "");
                    HttpHelper.postData(MyURL.UPDATE_LEVEL_BY_ID, qq, null);

                    if (Common.user.getLevel()<level) {
//                    HashMap<String, String> msgParam = new HashMap<>();
//                    msgParam.put("userID", "-1");
//                    msgParam.put("receiverID", Common.userID + "");
//                    msgParam.put("msgType", "20");
//                    msgParam.put("msgContent", "恭喜你升级啦~更多好玩功能等你来探索~");
//                    HttpHelper.postData(MyURL.INSERT_MSG, msgParam, null);
                        Common.user.setLevel(level);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            Message msg = handler.obtainMessage();
//            msg.what = 1;
//            msg.arg1 = Common.user.getLevel();
//            //msg.arg2 = Common.user.getExp();
//            handler.sendMessage(msg);

            //上传后将mCount置0
            Pedometer.mCount = 0;

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("TimeService:onStartCommand");
        //每隔deltaTime会调用

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        }).start();

        int sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int delataTime =10* 1000;//60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + delataTime;
        Intent i = new Intent(this, MyAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        if (sdkVersion>=19)
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        else
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        /**
         *
         ！第一个参数是整形参数，用来指定AlarmManager的工作类型
         ELAPSED_REALTIME表示让定时任务的触发时间从系统开机开始算起，但不会唤醒CPU。
         ELAPSED_REALTIME_WAKEUP同上，但是会唤醒CPU。
         RTC表示让定时任务的触发时间从1970年1月1日0点开始算起，但不会唤醒CPU。
         RTC_WAKEUP同上，但是会唤醒CPU。
         ！第二个参数用来指定定时任务触发的时间，单位为毫秒。
         如果第一个参数使用ELAPSED开头的参数就传入开机至今的时间加上延迟执行的时间。
         如果第一个参数使用RTC开头的参数就传入1970年1月1日0点至今的时间加上延迟执行的时间。
         使用SystemClock.elapsedRealtime()方法可以获取到系统开机至今所经历时间的毫秒数。
         使用System.currentTimeMillis()方法可以获取到1970年1月1日0点至今所经历时间的毫秒数。
         ！第三个参数是PendingIntent，用于指定一个未执行的意图。
         通过getBroadcast()方法获取一个能够执行广播的PendingIntent，当定时任务被触发时执行广播。
         */

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("TimeService:onDestroy");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("TimeService:onBind");
        return null;
    }


//    public static Handler handler = new Handler() { //更新UI
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    if (FragPersonalViewpagerRight.mTextViewMySteps!=null ) {
//                        FragPersonalViewpagerRight.mTextViewMySteps.setText("Lv." + msg.arg1 + "");
//                        //SingleBasicMapActivity.expBar.setMax(ExpUtil.getMaxExpByLevel(msg.arg1));
//                        //SingleBasicMapActivity.expBar.setProgress(ExpUtil.getCurrentBarExpByUserExp(msg.arg2, msg.arg1));
//                    }
//                    break;
//            }
//        }
//    };



}
