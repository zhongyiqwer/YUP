package com.wao.dogcat.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.jpush.JpushUtil;
import com.owo.model.DataAccess;
import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.module_a_login.widgets.FragLoginOrRegister;
import com.owo.module_b_main.AtyMain;
import com.owo.utils.Common;
import com.owo.utils.UtilLog;
import com.wao.dogcat.R;


import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 启动页面
 */
public class SplashScreenActivity extends Activity {

    public SharedPreferences sp;
    public static boolean isForeground = false;

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "JPush";
    private int userID = -1;
    private int userStatus = -1;
    private SharedPreferences.Editor editor;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(SplashScreenActivity.this);
        setTheme(R.style.AppTheme);
        this.setContentView(R.layout.splash_screen);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        Common.screenWidth = metric.widthPixels;  // 屏幕宽度（像素）
        Common.screenHeight = metric.heightPixels;  // 屏幕高度（像素）
        //检查权限，权限一定要给，且必须在登录之前，否则就会crash
        //而且2个权限最好一起申请并加上权限申请结果回调判断，否则也会crash
        Boolean checkPermissions = Common.checkPermissions(this);
        if (checkPermissions){
            goin();
        }

    }

    private void goin() {
        //应用区域
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        Common.visibleHeight = outRect.height();  //应用区域高度 (实际可见高度)


        Common.userSP = getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID", 0);
        userStatus = Common.userSP.getInt("status", 0);
        Set<String> tags = new HashSet<>();
        tags.add(userStatus + "");

        if (userID != 0 && userStatus != 0) {
            JPushInterface.setAliasAndTags(getApplicationContext(), userID + "", tags, new TagAliasCallback() {
                @Override
                public void gotResult(int responseCode, String alias, Set<String> tags) {
                    // TODO
                    if (responseCode == 0) {
                        //加载成功
                        Log.i("AliasAndTags", alias + " " + tags.toString());

                    }
                }
            });
        }
        registerMessageReceiver();  // used for receive msg

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                sp = getSharedPreferences("flagSP", 0);
                SharedPreferences.Editor editor1 = sp.edit();
                if (sp.getInt("flag", 0) == 0) {
                    DataAccess access = new DataAccess();
                    access.initDatabase(SplashScreenActivity.this);
                    editor1.putInt("flag", 1);
                    editor1.commit();

                    Intent intent1 = new Intent();
                    finish();
                    intent1.setClass(SplashScreenActivity.this, GuideActivity.class);
                    startActivity(intent1);

                } else {
                    Intent intent1 = new Intent();
                    finish();
                    if (userID == 0)
                        intent1.setClass(SplashScreenActivity.this, AtyLoginOrRegister.class);
                    else intent1.setClass(SplashScreenActivity.this, AtyMain.class);
                    startActivity(intent1);

                }

            }
        };
        timer.schedule(task, 1400);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Common.REQUEST_PERMISSION:
                if (grantResults.length>0) {
                    UtilLog.i("SplashScreenActivity", "请求权限个数=" + grantResults.length);
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            UtilLog.i("SplashScreenActivity1", "没有权限");
                            finish();
                            return;
                        }
                    }
                    goin();
                }else {
                    UtilLog.i("SplashScreenActivity1", "未知错误");
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!JpushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();


    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


}

