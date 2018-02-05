package com.wao.dogcat.controller;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.utils.Common;
import com.owo.utils.MyImageLoader;
import com.wao.dogcat.R;


import java.io.File;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.toolsfinal.StorageUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.socialization.Socialization;
import io.jchat.android.chatting.utils.SharePreferenceManager;
import io.jchat.android.database.UserEntry;
import io.jchat.android.receiver.NotificationClickEventReceiver;

/**
 * Created by ppssyyy on 2017-02-12.
 */
public class App extends com.activeandroid.app.Application {


    private static final String TAG = "JPush";

    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String TARGET_ID = "targetId";
    public static final String GROUP_ID = "groupId";
    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";

    protected void attachBaseContext(Context base) {
             super.attachBaseContext(base);
             MultiDex.install(this) ;
         }

    //在application中初始化sdk，这个初始化最好放在application的程序入口中，防止意外发生
    @Override
    public void onCreate() {
        super.onCreate();
        //////////////////////////极光推送//////////////////////////
        Log.d(TAG, "[JpushApplication] onCreate");
        //初始化JMessage-sdk，第二个参数表示开启漫游
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
        SharePreferenceManager.init(getApplicationContext(), JCHAT_CONFIGS);
        //设置Notification的模式
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        //////////////////////////百度地图//////////////////////////
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        ///////////////////////////分享&评论点赞组件////////////////////////
        //初始化分享组件
        ShareSDK.initSDK(this);
        ShareSDK.registerService(Socialization.class);


        ///////////////////////////图片相关/////////////////////////
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
                Common.IMG_CACHE_PATH);

        //设置主题
        //ThemeConfig theme = ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getApplicationContext().getResources().getColor(R.color.themeColor))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(this, new MyImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setTakePhotoFolder(cacheDir)
                //.setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);

        //universal-image-loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.ic_launcher) //
                .showImageOnFail(R.drawable.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存位置
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)//
                .diskCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);




    }
    public static void setPicturePath(String appKey) {
        if (!SharePreferenceManager.getCachedAppKey().equals(appKey)) {
            SharePreferenceManager.setCachedAppKey(appKey);
            PICTURE_DIR = "sdcard/JChatDemo/pictures/" + appKey + "/";
        }
    }

    public static UserEntry getUserEntry() {
        return UserEntry.getUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getAppKey());
    }


}
