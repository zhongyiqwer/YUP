/**
 * SettingActivity.java
 * 设置界面
 *
 * @author Peng Shiyao
 */
package com.wao.dogcat.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.utils.Common;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;


import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;

import java.util.HashMap;

/**
 * 消息详情
 */
public class MsgDetailActivity extends Activity implements View.OnClickListener {
    private int status = -1;
    private int sex = -1;
    private ImageView btnBack, pic2;
    private Button okBtn;
    private CircleImageView head;
    private TextView name, date, title, title2, ccontent;
    private LinearLayout navBar, ll1, capsulell;
    private HashMap<String, Object> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(MsgDetailActivity.this);
        this.setContentView(R.layout.msg_detail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        navBar = (LinearLayout) findViewById(R.id.navBar);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        title = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        capsulell = (LinearLayout) findViewById(R.id.capsulell);
        ccontent = (TextView) findViewById(R.id.content);
        head = (CircleImageView) findViewById(R.id.head);
        name = (TextView) findViewById(R.id.name);
        pic2 = (ImageView) findViewById(R.id.pic2);
        date = (TextView) findViewById(R.id.date);
        Common.userSP = getSharedPreferences("userSP", 0);
        status = Common.userSP.getInt("status", 0);
        sex = Common.userSP.getInt("sex", 0);
        okBtn = (Button) findViewById(R.id.okBtn);
        boolean flag = Common.isNetworkAvailable(MsgDetailActivity.this);
        if (!flag) {
            Common.display(MsgDetailActivity.this, "请开启手机网络");
        }


        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        if (content != null) {
            try {
                data = HttpHelper.AJgetCapsule(content);
                System.out.println("DDDDDDDDDDATA="+data);
                if (data != null) {
                    if(Common.half==null){
                        String jsonInfo = getHalfByID();
                        Common.setHalfInfo(HttpHelper.AJgetHalfByID(jsonInfo));
                    }
                    if (Common.half != null) {
                        title2.setText("收到了" + Common.half.getUserName() + "的时光沙漏");
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                                MyURL.ROOT + Common.half.getAvatar(), head, options);
                        ccontent.setText(data.get("content").toString());
                        name.setText(Common.half.getUserName());

                        DisplayImageOptions options1 = new DisplayImageOptions.Builder()//
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                                .showImageForEmptyUri(R.drawable.head) //
                                .showImageOnFail(R.drawable.head) //
                                .cacheInMemory(true) //
                                .cacheOnDisk(true) //
                                .build();//
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                                MyURL.ROOT +data.get("photo").toString() , pic2, options1);

                    } else Common.display(MsgDetailActivity.this, "获取对象信息失败");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        switch (intent.getIntExtra("extra", 0)) {
//            case 5:
//                okBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            HashMap<String, String> param = new HashMap<>();
//                            param.put("userID", Common.userID + "");
//                            param.put("itemID", "10");
//                            HttpHelper.postData(MyURL.INSERT_USER_ITEM, param, null);
//
//                            HashMap<String, String> pp = new HashMap<>();
//                            pp.put("id", Common.userID + "");
//                            pp.put("money", (Common.user.getMoney() + 500) + "");
//                            HttpHelper.postData(MyURL.UPDATE_MONEY_BY_ID, pp, null);
//                            Common.user.setMoney(Common.user.getMoney() + 500);
//
//                            Common.display(MsgDetailActivity.this, "领取成功");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//                break;
//
//        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(MsgDetailActivity.this, MsgActivity.class);
            finish();
            MsgDetailActivity.this.startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent();
                intent.setClass(MsgDetailActivity.this, MsgActivity.class);
                finish();
                MsgDetailActivity.this.startActivity(intent);
                break;
        }

    }

    public String getHalfByID() throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("loverAID", Common.userID + "");
        String result = HttpHelper.postData(MyURL.GET_HALF_BY_ID, paramHM, null);
        return result;
    }
}

