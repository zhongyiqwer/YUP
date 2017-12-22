/**
 * SettingActivity.java
 * 设置界面
 *
 * @author Peng Shiyao
 */
package com.wao.dogcat.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.owo.utils.Common;
import com.owo.utils.DateTimeHelper;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import java.util.HashMap;

/**
 * 消息详情
 */
public class UserMsgDetailActivity extends Activity implements View.OnClickListener {
    private Button okBtn;
    private CircleImageView head;
    private TextView name, date, content;
    private LinearLayout inputArea;
    private EditText editArea;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(UserMsgDetailActivity.this);
        this.setContentView(R.layout.user_msg_detail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        content = (TextView) findViewById(R.id.content);
        head = (CircleImageView) findViewById(R.id.head);
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        Common.userSP = getSharedPreferences("userSP", 0);
        okBtn = (Button) findViewById(R.id.okBtn);
        boolean flag = Common.isNetworkAvailable(UserMsgDetailActivity.this);
        if (!flag) {
            Common.display(UserMsgDetailActivity.this, "请开启手机网络");
        }
        Intent intent = getIntent();
        String msgcontent = intent.getStringExtra("content");
        String avatar = intent.getStringExtra("avatar");
        String username = intent.getStringExtra("name");
        String pubdate = intent.getStringExtra("date");
        id = intent.getIntExtra("id",0);


        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                .showImageForEmptyUri(R.drawable.my) //
                .showImageOnFail(R.drawable.my) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(
                MyURL.ROOT +avatar , head, options);
       String formatDate = DateTimeHelper.timeLogic(DateTimeHelper
                        .timeMillis2FormatTime(pubdate, DateTimeHelper.DATE_FORMAT_TILL_SECOND),
                DateTimeHelper.DATE_FORMAT_TILL_SECOND);
        name.setText(username);
        date.setText(formatDate);
        content.setText(msgcontent);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(UserMsgDetailActivity.this, MsgActivity.class);
            finish();
            UserMsgDetailActivity.this.startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent();
                intent.setClass(UserMsgDetailActivity.this, MsgActivity.class);
                finish();
                UserMsgDetailActivity.this.startActivity(intent);
                break;
            case R.id.okBtn:
                LayoutInflater inflater = LayoutInflater.from(UserMsgDetailActivity.this);
                inputArea = (LinearLayout) inflater.inflate(R.layout.input_area, null); //parent
                editArea = (EditText) inputArea.findViewById(R.id.editArea); //child
                editArea.setHint("和TA多聊几句吧~");
                new AlertDialog.Builder(UserMsgDetailActivity.this)
                        .setTitle("消息回复")
                        .setView(inputArea)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        if (editArea.getText().toString().trim().length()!=0) {
                                            insertMsg(id + "", "100", editArea.getText().toString() + "_" + Common.userID + "_" + Common.user.getAvatar()
                                                    + "_" + Common.user.getUserName());
                                            Common.display(UserMsgDetailActivity.this, "消息发送成功");
                                        }else  Common.display(UserMsgDetailActivity.this, "消息不能为空哦");
                                    }
                                }

                        )
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }

    }
    public void insertMsg(String receiverID,String type,String content){
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userID", Common.userID + "");
            hashMap.put("receiverID", receiverID);
            hashMap.put("msgType", type);
            hashMap.put("msgContent", content);
            HttpHelper.postData(MyURL.INSERT_MSG, hashMap, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

