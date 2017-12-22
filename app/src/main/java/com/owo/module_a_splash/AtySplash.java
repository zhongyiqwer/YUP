package com.owo.module_a_splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.wao.dogcat.R;
import com.owo.model.DataAccess;
import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.utils.Common;
import com.owo.utils.util_aty.MyActivityManager;
import com.owo.utils.util_jpush.JpushUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 初始界面，延时，并在里面进行一些判断
 *
 * @author XQF
 * @created 2017/5/19
 */
public class AtySplash extends AppCompatActivity {

    //这个是用来记录用户有没有安装过？，没有安装过的话就是初始化一下数据库
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
        mam.pushOneActivity(AtySplash.this);
        setTheme(R.style.AppTheme);
        this.setContentView(R.layout.aty_splash_layout);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        Common.screenWidth = metric.widthPixels;  // 屏幕宽度（像素）
        Common.screenHeight = metric.heightPixels;  // 屏幕高度（像素）

        //应用区域
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        Common.visibleHeight = outRect.height();  //应用区域高度 (实际可见高度)




        /**
         * 初始化一下本地保存的id
         */
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

                    //初始化一下本地数据库
                    DataAccess access = new DataAccess();
                    access.initDatabase(AtySplash.this);
                    editor1.putInt("flag", 1);
                    editor1.commit();

                    Intent intent1 = new Intent();
                    finish();
                    intent1.setClass(AtySplash.this, AtyLoginOrRegister.class);
                    startActivity(intent1);

                } else {
                    Intent intent1 = new Intent();
                    finish();
                    intent1.setClass(AtySplash.this, AtyLoginOrRegister.class);
                    startActivity(intent1);

                }
            }
        };
        timer.schedule(task, 1400);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    /**
     * 注册光博
     */
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

