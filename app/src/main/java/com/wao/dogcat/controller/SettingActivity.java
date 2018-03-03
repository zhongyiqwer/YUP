/**
 * SettingActivity.java
 *设置界面
 * @author 	Peng Shiyao
 */
package com.wao.dogcat.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.owo.module_a_login.AtyLoginOrRegister;
import com.owo.module_b_main.AtyMain;
import com.owo.module_b_personal.bean.BeanUserCommentLabel;
import com.owo.module_b_personal.widgets.FragPersonalViewpagerRight;
import com.owo.utils.Common;
import com.owo.utils.FileManager;
import com.owo.utils.UpdateManager;
import com.wao.dogcat.R;


import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * 设置
 */

public class SettingActivity extends Activity implements View.OnClickListener {
    private TextView versionTxt,logoutTxt,title,myCache;
    private LinearLayout changePwll, feedBackll, versionInfoll, detachll, logoutll,navBar, clearCache;
    private Switch switchBtn;
    private String versionCode;
    private int status = -1;
    private ImageView btnBack;
    private SharedPreferences settingSP;
    private SharedPreferences.Editor editor;
    private String fileSizeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(SettingActivity.this);
        this.setContentView(R.layout.setting);
        navBar = (LinearLayout) findViewById(R.id.navBar);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        versionTxt = (TextView) findViewById(R.id.versionTxt);
        versionTxt.setText( UpdateManager.getVersionName(this)+"");
        logoutTxt = (TextView) findViewById(R.id.logoutTxt);
        myCache = (TextView) findViewById(R.id.myCache);
        //detachll = (LinearLayout) findViewById(R.id.detachll);
        title = (TextView) findViewById(R.id.title);
        switchBtn = (Switch) findViewById(R.id.switchPush);
        Common.userSP = getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);
        settingSP = getSharedPreferences("settingSP", 0);
        boolean flag = Common.isNetworkAvailable(SettingActivity.this);
        if (!flag){
           Common.display(SettingActivity.this,"请开启手机网络");
        }


        if (settingSP.getInt("push",0)==1) {
            switchBtn.setChecked(true);
            //switchBtn.setBackgroundResource(R.drawable.swich_open_yellow);
        }
        if (settingSP.getInt("push",0)==-1){
            switchBtn.setChecked(false);
            //switchBtn.setBackgroundResource(R.drawable.swich_close_yellow);
        }
        fileSizeString = FileManager
                .fileSizeCal(new File(Common.FULL_IMG_CACHE_PATH));
        myCache.setText(fileSizeString);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //开启
                    editor = settingSP.edit();
                    editor.putInt("push",1);
                    editor.commit();
                    JPushInterface.resumePush(SettingActivity.this);
                    //switchBtn.setBackgroundResource(R.drawable.swich_open_yellow);
                }else {
                    //关闭
                    editor = settingSP.edit();
                    editor.putInt("push",-1);
                    editor.commit();
                    JPushInterface.stopPush(SettingActivity.this);
                    //switchBtn.setBackgroundResource(R.drawable.swich_close_yellow);
                }
            }
        });




//        JPushInterface.stopPush(this);
//        JPushInterface.resumePush(this);





    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Intent intent = new Intent();
            finish();
            //intent.setClass(SettingActivity.this, AtyMain.class);
            //startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        if (view == btnBack){
            //Intent intent = new Intent();
            finish();
            //intent.setClass(SettingActivity.this, AtyMain.class);
           // startActivity(intent);
        }

        switch (view.getId()){
            case R.id.clearCachell:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("确定清除缓存？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (FileManager.delAllFile(Common.FULL_IMG_CACHE_PATH)) {
                            Common.display(SettingActivity.this, "缓存已清空");
                        } else
                            Common.display(SettingActivity.this, "清空缓存失败");
                        fileSizeString = FileManager
                                .fileSizeCal(new File(Common.FULL_IMG_CACHE_PATH));
                        myCache.setText(fileSizeString);

                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
                break;
            case R.id.offlinell:
                Intent i = new Intent();
                i.setClass(SettingActivity.this, OfflineMapActivity.class);
                finish();
                SettingActivity.this.startActivity(i);
                break;
            case R.id.changePwll:
                //Common.display(SettingActivity.this, "敬请期待");
                //更改密码
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this,ChangePassWordActivity.class);
                finish();
                SettingActivity.this.startActivity(intent);
                break;
            case R.id.feedBackll:
                Common.display(SettingActivity.this, "敬请期待");
                break;
            case R.id.versionInfoll:
                Common.display(SettingActivity.this, "已是最新版本");
                break;
            case R.id.logoutll:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingActivity.this);
                builder1.setMessage("确定退出当前账号？");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FragPersonalViewpagerRight.mUserLabels=new ArrayList<BeanUserCommentLabel>();
                        Common.clearAll();
                        Intent intent = new Intent();
                        finish();
                        intent.putExtra("extra", "logout");
                        intent.setClass(SettingActivity.this, AtyLoginOrRegister.class);
                        startActivity(intent);
                        JMessageClient.logout();
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder1.create().show();
                break;
        }

    }



}

